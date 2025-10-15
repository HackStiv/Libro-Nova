package com.libronova.service;

import com.libronova.dao.LoanDAO;
import com.libronova.model.Loan;
import com.libronova.util.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión de préstamos con validaciones de negocio
 */
public class LoanService {
    private final LoanDAO loanDAO;
    private final BookService bookService;
    private final MemberService memberService;
    private static final Logger logger = Logger.getInstance();

    // Configuración de préstamos
    private static final int LOAN_DAYS = 14; // Días de préstamo por defecto
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("5.00"); // Multa diaria

    public LoanService() {
        this.loanDAO = new LoanDAO();
        this.bookService = new BookService();
        this.memberService = new MemberService();
    }

    /**
     * Crea un nuevo préstamo con validaciones
     */
    public boolean createLoan(int bookId, int memberId, int userId) throws Exception {
        try {
            // Validar que el libro existe y está disponible
            bookService.validateStockAvailability(bookId, 1);

            // Validar que el miembro puede realizar préstamos
            memberService.validateMemberCanBorrow(memberId);

            // Crear el préstamo
            String loanId = generateLoanId();
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);

            Loan loan = new Loan(loanId, bookId, memberId, userId, loanDate, dueDate);

            // Crear el préstamo en la base de datos
            boolean loanCreated = loanDAO.create(loan);
            if (!loanCreated) {
                throw new RuntimeException("Error al crear el préstamo en la base de datos");
            }

            // Actualizar stock del libro
            boolean stockUpdated = bookService.borrowBook(bookId);
            if (!stockUpdated) {
                // Si falla la actualización del stock, eliminar el préstamo
                loanDAO.delete(loan.getId());
                throw new RuntimeException("Error al actualizar el stock del libro");
            }

            // Actualizar contador de préstamos del miembro
            boolean memberUpdated = memberService.incrementMemberLoans(memberId);
            if (!memberUpdated) {
                // Si falla la actualización del miembro, revertir cambios
                bookService.returnBook(bookId);
                loanDAO.delete(loan.getId());
                throw new RuntimeException("Error al actualizar el contador de préstamos del miembro");
            }

            logger.info("Préstamo creado exitosamente: " + loanId);
            return true;

        } catch (Exception e) {
            logger.error("Error al crear préstamo: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Busca un préstamo por ID
     */
    public Loan findLoanById(int id) {
        return loanDAO.findById(id);
    }

    /**
     * Busca un préstamo por loan_id
     */
    public Loan findLoanByLoanId(String loanId) {
        return loanDAO.findByLoanId(loanId);
    }

    /**
     * Obtiene todos los préstamos activos
     */
    public List<Loan> getActiveLoans() {
        return loanDAO.findActiveLoans();
    }

    /**
     * Obtiene préstamos vencidos
     */
    public List<Loan> getOverdueLoans() {
        return loanDAO.findOverdueLoans();
    }

    /**
     * Obtiene préstamos de un miembro
     */
    public List<Loan> getLoansByMember(int memberId) {
        return loanDAO.findByMember(memberId);
    }

    /**
     * Obtiene préstamos activos de un miembro
     */
    public List<Loan> getActiveLoansByMember(int memberId) {
        return loanDAO.findActiveByMember(memberId);
    }

    /**
     * Obtiene préstamos de un libro
     */
    public List<Loan> getLoansByBook(int bookId) {
        return loanDAO.findByBook(bookId);
    }

    /**
     * Busca préstamos por término de búsqueda
     */
    public List<Loan> searchLoans(String searchTerm) {
        return loanDAO.search(searchTerm);
    }

    /**
     * Devuelve un libro (marca el préstamo como devuelto)
     */
    public boolean returnBook(int loanId) throws Exception {
        try {
            Loan loan = loanDAO.findById(loanId);
            if (loan == null) {
                throw new RuntimeException("Préstamo no encontrado con ID: " + loanId);
            }

            if (loan.isReturned()) {
                throw new IllegalStateException("El libro ya ha sido devuelto");
            }

            // Marcar préstamo como devuelto
            loan.markAsReturned();
            
            // Calcular multa si aplica
            if (loan.isOverdue()) {
                BigDecimal fineAmount = loan.calculateFine(DAILY_FINE_RATE);
                loan.setFineAmount(fineAmount);
                logger.info("Multa calculada para préstamo " + loan.getLoanId() + ": $" + fineAmount);
            }

            // Actualizar en la base de datos
            boolean loanUpdated = loanDAO.update(loan);
            if (!loanUpdated) {
                throw new RuntimeException("Error al actualizar el préstamo");
            }

            // Actualizar stock del libro
            boolean stockUpdated = bookService.returnBook(loan.getBookId());
            if (!stockUpdated) {
                logger.warning("Error al actualizar el stock del libro, pero el préstamo fue marcado como devuelto");
            }

            // Actualizar contador de préstamos del miembro
            boolean memberUpdated = memberService.decrementMemberLoans(loan.getMemberId());
            if (!memberUpdated) {
                logger.warning("Error al actualizar el contador de préstamos del miembro, pero el préstamo fue marcado como devuelto");
            }

            logger.info("Libro devuelto exitosamente: " + loan.getLoanId());
            return true;

        } catch (Exception e) {
            logger.error("Error al devolver libro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Marca préstamos vencidos como OVERDUE
     */
    public void updateOverdueLoans() {
        try {
            List<Loan> activeLoans = loanDAO.findActiveLoans();
            int updatedCount = 0;

            for (Loan loan : activeLoans) {
                if (loan.isOverdue()) {
                    loan.markAsOverdue();
                    loanDAO.updateStatus(loan.getId(), "OVERDUE");
                    updatedCount++;
                }
            }

            if (updatedCount > 0) {
                logger.info("Se actualizaron " + updatedCount + " préstamos vencidos");
            }

        } catch (Exception e) {
            logger.error("Error al actualizar préstamos vencidos: " + e.getMessage(), e);
        }
    }

    /**
     * Calcula la multa de un préstamo
     */
    public BigDecimal calculateFine(int loanId) {
        Loan loan = loanDAO.findById(loanId);
        if (loan == null) {
            throw new RuntimeException("Préstamo no encontrado con ID: " + loanId);
        }

        return loan.calculateFine(DAILY_FINE_RATE);
    }

    /**
     * Obtiene estadísticas de préstamos
     */
    public String getLoanStatistics() {
        try {
            List<Loan> activeLoans = loanDAO.findActiveLoans();
            List<Loan> overdueLoans = loanDAO.findOverdueLoans();

            int totalActive = activeLoans.size();
            int totalOverdue = overdueLoans.size();

            BigDecimal totalFines = BigDecimal.ZERO;
            for (Loan loan : overdueLoans) {
                totalFines = totalFines.add(loan.getFineAmount());
            }

            return String.format(
                "=== ESTADÍSTICAS DE PRÉSTAMOS ===\n" +
                "Préstamos activos: %d\n" +
                "Préstamos vencidos: %d\n" +
                "Total de multas: $%.2f\n" +
                "Fecha de consulta: %s",
                totalActive, totalOverdue, totalFines, 
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: " + e.getMessage(), e);
            return "Error al obtener estadísticas de préstamos";
        }
    }

    /**
     * Genera un ID único para el préstamo
     */
    private String generateLoanId() {
        return "LOAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Obtiene la configuración de días de préstamo
     */
    public static int getLoanDays() {
        return LOAN_DAYS;
    }

    /**
     * Obtiene la tasa de multa diaria
     */
    public static BigDecimal getDailyFineRate() {
        return DAILY_FINE_RATE;
    }
}
