package com.libronova.ui;

import com.libronova.model.Book;
import com.libronova.model.Member;
import com.libronova.model.Loan;
import com.libronova.service.BookService;
import com.libronova.service.MemberService;
import com.libronova.service.LoanService;
import com.libronova.util.Logger;
import com.libronova.util.CSVExporter;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Interfaz de usuario principal del sistema LibroNova
 */
public class LibroNovaUI {
    private final BookService bookService;
    private final MemberService memberService;
    private final LoanService loanService;
    private static final Logger logger = Logger.getInstance();

    public LibroNovaUI() {
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.loanService = new LoanService();
    }

    /**
     * Muestra el menú principal
     */
    public void showMainMenu() {
        while (true) {
            String[] options = {
                "Gestión de Libros",
                "Gestión de Miembros", 
                "Gestión de Préstamos",
                "Reportes",
                "Configuración",
                "Salir"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "=== SISTEMA LIBRONOVA ===\n" +
                "Sistema de Gestión de Bibliotecas\n\n" +
                "Seleccione una opción:",
                "Menú Principal",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0:
                    showBookMenu();
                    break;
                case 1:
                    showMemberMenu();
                    break;
                case 2:
                    showLoanMenu();
                    break;
                case 3:
                    showReportsMenu();
                    break;
                case 4:
                    showConfigMenu();
                    break;
                case 5:
                case JOptionPane.CLOSED_OPTION:
                    JOptionPane.showMessageDialog(null, "¡Gracias por usar LibroNova!");
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Muestra el menú de gestión de libros
     */
    private void showBookMenu() {
        while (true) {
            String[] options = {
                "Agregar Libro",
                "Buscar Libro",
                "Listar Libros",
                "Actualizar Libro",
                "Eliminar Libro",
                "Volver al Menú Principal"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "=== GESTIÓN DE LIBROS ===\n\nSeleccione una opción:",
                "Libros",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0:
                    addBook();
                    break;
                case 1:
                    searchBook();
                    break;
                case 2:
                    listBooks();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                case JOptionPane.CLOSED_OPTION:
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Muestra el menú de gestión de miembros
     */
    private void showMemberMenu() {
        while (true) {
            String[] options = {
                "Agregar Miembro",
                "Buscar Miembro",
                "Listar Miembros",
                "Actualizar Miembro",
                "Eliminar Miembro",
                "Volver al Menú Principal"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "=== GESTIÓN DE MIEMBROS ===\n\nSeleccione una opción:",
                "Miembros",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0:
                    addMember();
                    break;
                case 1:
                    searchMember();
                    break;
                case 2:
                    listMembers();
                    break;
                case 3:
                    updateMember();
                    break;
                case 4:
                    deleteMember();
                    break;
                case 5:
                case JOptionPane.CLOSED_OPTION:
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Muestra el menú de gestión de préstamos
     */
    private void showLoanMenu() {
        while (true) {
            String[] options = {
                "Realizar Préstamo",
                "Devolver Libro",
                "Buscar Préstamo",
                "Listar Préstamos Activos",
                "Listar Préstamos Vencidos",
                "Estadísticas",
                "Volver al Menú Principal"
            };

            int choice = JOptionPane.showOptionDialog(
                null,
                "=== GESTIÓN DE PRÉSTAMOS ===\n\nSeleccione una opción:",
                "Préstamos",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0:
                    createLoan();
                    break;
                case 1:
                    returnBook();
                    break;
                case 2:
                    searchLoan();
                    break;
                case 3:
                    listActiveLoans();
                    break;
                case 4:
                    listOverdueLoans();
                    break;
                case 5:
                    showLoanStatistics();
                    break;
                case 6:
                case JOptionPane.CLOSED_OPTION:
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Muestra el menú de reportes
     */
    private void showReportsMenu() {
        String[] options = {
            "Exportar Catálogo de Libros",
            "Exportar Préstamos",
            "Volver al Menú Principal"
        };

        int choice = JOptionPane.showOptionDialog(
            null,
            "=== REPORTES ===\n\nSeleccione una opción:",
            "Reportes",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                exportBookCatalog();
                break;
            case 1:
                exportLoans();
                break;
            case 2:
            case JOptionPane.CLOSED_OPTION:
                return;
            default:
                break;
        }
    }

    /**
     * Muestra el menú de configuración
     */
    private void showConfigMenu() {
        String[] options = {
            "Ver Configuración",
            "Volver al Menú Principal"
        };

        int choice = JOptionPane.showOptionDialog(
            null,
            "=== CONFIGURACIÓN ===\n\nSeleccione una opción:",
            "Configuración",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                showConfiguration();
                break;
            case 1:
            case JOptionPane.CLOSED_OPTION:
                return;
            default:
                break;
        }
    }

    /**
     * Agrega un nuevo libro
     */
    private void addBook() {
        try {
            String isbn = JOptionPane.showInputDialog("Ingrese el ISBN del libro:");
            if (isbn == null || isbn.trim().isEmpty()) return;

            String title = JOptionPane.showInputDialog("Ingrese el título del libro:");
            if (title == null || title.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog("Ingrese el autor del libro:");
            if (author == null || author.trim().isEmpty()) return;

            String publisher = JOptionPane.showInputDialog("Ingrese la editorial:");
            if (publisher == null || publisher.trim().isEmpty()) return;

            String category = JOptionPane.showInputDialog("Ingrese la categoría:");
            if (category == null || category.trim().isEmpty()) return;

            String stockStr = JOptionPane.showInputDialog("Ingrese el stock inicial:");
            if (stockStr == null || stockStr.trim().isEmpty()) return;

            int stock = Integer.parseInt(stockStr);

            Book book = new Book(isbn, title, author, publisher, LocalDate.now(), category, stock);
            
            if (bookService.createBook(book)) {
                JOptionPane.showMessageDialog(null, "Libro agregado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el libro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al agregar libro: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un libro
     */
    private void searchBook() {
        String searchTerm = JOptionPane.showInputDialog("Ingrese el término de búsqueda (título, autor o ISBN):");
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        try {
            List<Book> books = bookService.searchBooks(searchTerm);
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron libros con ese criterio.");
            } else {
                showBookList(books, "Resultados de Búsqueda");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al buscar libros: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos los libros
     */
    private void listBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay libros registrados.");
            } else {
                showBookList(books, "Catálogo de Libros");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al listar libros: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra una lista de libros en un diálogo
     */
    private void showBookList(List<Book> books, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        
        for (Book book : books) {
            sb.append("ID: ").append(book.getId()).append("\n");
            sb.append("ISBN: ").append(book.getIsbn()).append("\n");
            sb.append("Título: ").append(book.getTitle()).append("\n");
            sb.append("Autor: ").append(book.getAuthor()).append("\n");
            sb.append("Editorial: ").append(book.getPublisher()).append("\n");
            sb.append("Categoría: ").append(book.getCategory()).append("\n");
            sb.append("Stock: ").append(book.getStock()).append("\n");
            sb.append("Disponible: ").append(book.getAvailableStock()).append("\n");
            sb.append("Estado: ").append(book.isActive() ? "Activo" : "Inactivo").append("\n");
            sb.append("---\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    /**
     * Actualiza un libro
     */
    private void updateBook() {
        try {
            String idStr = JOptionPane.showInputDialog("Ingrese el ID del libro a actualizar:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);
            Book book = bookService.findBookById(id);

            String title = JOptionPane.showInputDialog("Título actual: " + book.getTitle() + "\nIngrese el nuevo título:");
            if (title != null && !title.trim().isEmpty()) {
                book.setTitle(title);
            }

            String author = JOptionPane.showInputDialog("Autor actual: " + book.getAuthor() + "\nIngrese el nuevo autor:");
            if (author != null && !author.trim().isEmpty()) {
                book.setAuthor(author);
            }

            if (bookService.updateBook(book)) {
                JOptionPane.showMessageDialog(null, "Libro actualizado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el libro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al actualizar libro: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un libro
     */
    private void deleteBook() {
        try {
            String idStr = JOptionPane.showInputDialog("Ingrese el ID del libro a eliminar:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);
            
            int confirm = JOptionPane.showConfirmDialog(
                null, 
                "¿Está seguro de que desea eliminar este libro?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (bookService.deleteBook(id)) {
                    JOptionPane.showMessageDialog(null, "Libro eliminado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el libro.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al eliminar libro: " + e.getMessage(), e);
        }
    }

    /**
     * Agrega un nuevo miembro
     */
    private void addMember() {
        try {
            String memberId = JOptionPane.showInputDialog("Ingrese el ID del miembro:");
            if (memberId == null || memberId.trim().isEmpty()) return;

            String firstName = JOptionPane.showInputDialog("Ingrese el nombre:");
            if (firstName == null || firstName.trim().isEmpty()) return;

            String lastName = JOptionPane.showInputDialog("Ingrese el apellido:");
            if (lastName == null || lastName.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog("Ingrese el email:");
            if (email == null || email.trim().isEmpty()) return;

            String phone = JOptionPane.showInputDialog("Ingrese el teléfono:");
            if (phone == null || phone.trim().isEmpty()) return;

            String address = JOptionPane.showInputDialog("Ingrese la dirección:");
            if (address == null || address.trim().isEmpty()) return;

            String[] membershipTypes = {"REGULAR", "PREMIUM", "VIP"};
            String membershipType = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de membresía:",
                "Tipo de Membresía",
                JOptionPane.QUESTION_MESSAGE,
                null,
                membershipTypes,
                membershipTypes[0]
            );

            if (membershipType == null) return;

            Member member = new Member(memberId, firstName, lastName, email, phone, address, LocalDate.now(), membershipType);
            
            if (memberService.createMember(member)) {
                JOptionPane.showMessageDialog(null, "Miembro agregado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el miembro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al agregar miembro: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un miembro
     */
    private void searchMember() {
        String searchTerm = JOptionPane.showInputDialog("Ingrese el término de búsqueda (nombre, apellido, ID o email):");
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        try {
            List<Member> members = memberService.searchMembers(searchTerm);
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron miembros con ese criterio.");
            } else {
                showMemberList(members, "Resultados de Búsqueda");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al buscar miembros: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos los miembros
     */
    private void listMembers() {
        try {
            List<Member> members = memberService.getAllMembers();
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay miembros registrados.");
            } else {
                showMemberList(members, "Lista de Miembros");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al listar miembros: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra una lista de miembros en un diálogo
     */
    private void showMemberList(List<Member> members, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        
        for (Member member : members) {
            sb.append("ID: ").append(member.getId()).append("\n");
            sb.append("ID Miembro: ").append(member.getMemberId()).append("\n");
            sb.append("Nombre: ").append(member.getFullName()).append("\n");
            sb.append("Email: ").append(member.getEmail()).append("\n");
            sb.append("Teléfono: ").append(member.getPhone()).append("\n");
            sb.append("Tipo: ").append(member.getMembershipType()).append("\n");
            sb.append("Préstamos: ").append(member.getCurrentLoans()).append("/").append(member.getMaxLoans()).append("\n");
            sb.append("Estado: ").append(member.isActive() ? "Activo" : "Inactivo").append("\n");
            sb.append("---\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    /**
     * Actualiza un miembro
     */
    private void updateMember() {
        try {
            String idStr = JOptionPane.showInputDialog("Ingrese el ID del miembro a actualizar:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);
            Member member = memberService.findMemberById(id);

            String firstName = JOptionPane.showInputDialog("Nombre actual: " + member.getFirstName() + "\nIngrese el nuevo nombre:");
            if (firstName != null && !firstName.trim().isEmpty()) {
                member.setFirstName(firstName);
            }

            String lastName = JOptionPane.showInputDialog("Apellido actual: " + member.getLastName() + "\nIngrese el nuevo apellido:");
            if (lastName != null && !lastName.trim().isEmpty()) {
                member.setLastName(lastName);
            }

            if (memberService.updateMember(member)) {
                JOptionPane.showMessageDialog(null, "Miembro actualizado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el miembro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al actualizar miembro: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un miembro
     */
    private void deleteMember() {
        try {
            String idStr = JOptionPane.showInputDialog("Ingrese el ID del miembro a eliminar:");
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr);
            
            int confirm = JOptionPane.showConfirmDialog(
                null, 
                "¿Está seguro de que desea eliminar este miembro?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (memberService.deleteMember(id)) {
                    JOptionPane.showMessageDialog(null, "Miembro eliminado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el miembro.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al eliminar miembro: " + e.getMessage(), e);
        }
    }

    /**
     * Crea un nuevo préstamo
     */
    private void createLoan() {
        try {
            String bookIdStr = JOptionPane.showInputDialog("Ingrese el ID del libro:");
            if (bookIdStr == null || bookIdStr.trim().isEmpty()) return;

            String memberIdStr = JOptionPane.showInputDialog("Ingrese el ID del miembro:");
            if (memberIdStr == null || memberIdStr.trim().isEmpty()) return;

            int bookId = Integer.parseInt(bookIdStr);
            int memberId = Integer.parseInt(memberIdStr);
            int userId = 1; // Usuario por defecto

            if (loanService.createLoan(bookId, memberId, userId)) {
                JOptionPane.showMessageDialog(null, "Préstamo realizado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al realizar el préstamo.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al crear préstamo: " + e.getMessage(), e);
        }
    }

    /**
     * Devuelve un libro
     */
    private void returnBook() {
        try {
            String loanIdStr = JOptionPane.showInputDialog("Ingrese el ID del préstamo a devolver:");
            if (loanIdStr == null || loanIdStr.trim().isEmpty()) return;

            int loanId = Integer.parseInt(loanIdStr);

            if (loanService.returnBook(loanId)) {
                JOptionPane.showMessageDialog(null, "Libro devuelto exitosamente!");
            } else {
                JOptionPane.showMessageDialog(null, "Error al devolver el libro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al devolver libro: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un préstamo
     */
    private void searchLoan() {
        String searchTerm = JOptionPane.showInputDialog("Ingrese el término de búsqueda:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        try {
            List<Loan> loans = loanService.searchLoans(searchTerm);
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se encontraron préstamos con ese criterio.");
            } else {
                showLoanList(loans, "Resultados de Búsqueda");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al buscar préstamos: " + e.getMessage(), e);
        }
    }

    /**
     * Lista préstamos activos
     */
    private void listActiveLoans() {
        try {
            List<Loan> loans = loanService.getActiveLoans();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos activos.");
            } else {
                showLoanList(loans, "Préstamos Activos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al listar préstamos activos: " + e.getMessage(), e);
        }
    }

    /**
     * Lista préstamos vencidos
     */
    private void listOverdueLoans() {
        try {
            List<Loan> loans = loanService.getOverdueLoans();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos vencidos.");
            } else {
                showLoanList(loans, "Préstamos Vencidos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al listar préstamos vencidos: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra una lista de préstamos en un diálogo
     */
    private void showLoanList(List<Loan> loans, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(title).append(" ===\n\n");
        
        for (Loan loan : loans) {
            sb.append("ID: ").append(loan.getId()).append("\n");
            sb.append("ID Préstamo: ").append(loan.getLoanId()).append("\n");
            sb.append("Libro ID: ").append(loan.getBookId()).append("\n");
            sb.append("Miembro ID: ").append(loan.getMemberId()).append("\n");
            sb.append("Fecha Préstamo: ").append(loan.getLoanDate()).append("\n");
            sb.append("Fecha Vencimiento: ").append(loan.getDueDate()).append("\n");
            if (loan.getReturnDate() != null) {
                sb.append("Fecha Devolución: ").append(loan.getReturnDate()).append("\n");
            }
            sb.append("Estado: ").append(loan.getStatus()).append("\n");
            if (loan.getFineAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                sb.append("Multa: $").append(loan.getFineAmount()).append("\n");
            }
            sb.append("---\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    /**
     * Muestra estadísticas de préstamos
     */
    private void showLoanStatistics() {
        try {
            String stats = loanService.getLoanStatistics();
            JOptionPane.showMessageDialog(null, stats);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al obtener estadísticas: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta el catálogo de libros
     */
    private void exportBookCatalog() {
        try {
            List<Book> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay libros para exportar.");
                return;
            }

            String filename = CSVExporter.generateFilename("catalogo_libros", "csv");
            if (CSVExporter.exportBookCatalog(books, filename)) {
                JOptionPane.showMessageDialog(null, "Catálogo de libros exportado exitosamente:\n" + filename);
            } else {
                JOptionPane.showMessageDialog(null, "Error al exportar el catálogo de libros.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al exportar catálogo: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta los préstamos
     */
    private void exportLoans() {
        try {
            List<Loan> loans = loanService.getActiveLoans();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos para exportar.");
                return;
            }

            String filename = CSVExporter.generateFilename("prestamos_activos", "csv");
            if (CSVExporter.exportLoans(loans, filename)) {
                JOptionPane.showMessageDialog(null, "Préstamos exportados exitosamente:\n" + filename);
            } else {
                JOptionPane.showMessageDialog(null, "Error al exportar los préstamos.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            logger.error("Error al exportar préstamos: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra la configuración del sistema
     */
    private void showConfiguration() {
        String config = "=== CONFIGURACIÓN DEL SISTEMA ===\n\n" +
                       "Días de préstamo: " + LoanService.getLoanDays() + "\n" +
                       "Multa diaria: $" + LoanService.getDailyFineRate() + "\n" +
                       "Fecha actual: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        JOptionPane.showMessageDialog(null, config);
    }
}
