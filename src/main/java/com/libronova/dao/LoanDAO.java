package com.libronova.dao;

import com.libronova.config.DatabaseConfig;
import com.libronova.model.Loan;
import com.libronova.util.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de préstamos
 */
public class LoanDAO {
    private static final Logger logger = Logger.getInstance();

    // Consultas SQL
    private static final String INSERT_LOAN = 
        "INSERT INTO loans (loan_id, book_id, member_id, user_id, loan_date, due_date, " +
        "return_date, status, fine_amount, notes, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID = 
        "SELECT * FROM loans WHERE id = ?";

    private static final String SELECT_BY_LOAN_ID = 
        "SELECT * FROM loans WHERE loan_id = ?";

    private static final String SELECT_ACTIVE_LOANS = 
        "SELECT * FROM loans WHERE status = 'ACTIVE' ORDER BY due_date";

    private static final String SELECT_OVERDUE_LOANS = 
        "SELECT * FROM loans WHERE status = 'OVERDUE' ORDER BY due_date";

    private static final String SELECT_BY_MEMBER = 
        "SELECT * FROM loans WHERE member_id = ? ORDER BY loan_date DESC";

    private static final String SELECT_BY_BOOK = 
        "SELECT * FROM loans WHERE book_id = ? ORDER BY loan_date DESC";

    private static final String SELECT_ACTIVE_BY_MEMBER = 
        "SELECT * FROM loans WHERE member_id = ? AND status = 'ACTIVE' ORDER BY due_date";

    private static final String UPDATE_LOAN = 
        "UPDATE loans SET return_date = ?, status = ?, fine_amount = ?, notes = ?, updated_at = ? WHERE id = ?";

    private static final String UPDATE_STATUS = 
        "UPDATE loans SET status = ?, updated_at = ? WHERE id = ?";

    private static final String UPDATE_FINE = 
        "UPDATE loans SET fine_amount = ?, updated_at = ? WHERE id = ?";

    private static final String DELETE_LOAN = 
        "DELETE FROM loans WHERE id = ?";

    private static final String SEARCH_LOANS = 
        "SELECT l.* FROM loans l " +
        "JOIN members m ON l.member_id = m.id " +
        "JOIN books b ON l.book_id = b.id " +
        "WHERE (m.first_name LIKE ? OR m.last_name LIKE ? OR m.member_id LIKE ? " +
        "OR b.title LIKE ? OR b.isbn LIKE ? OR l.loan_id LIKE ?) " +
        "ORDER BY l.loan_date DESC";

    /**
     * Crea un nuevo préstamo
     */
    public boolean create(Loan loan) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_LOAN, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, loan.getLoanId());
            stmt.setInt(2, loan.getBookId());
            stmt.setInt(3, loan.getMemberId());
            stmt.setInt(4, loan.getUserId());
            stmt.setDate(5, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(6, Date.valueOf(loan.getDueDate()));
            stmt.setObject(7, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            stmt.setString(8, loan.getStatus());
            stmt.setBigDecimal(9, loan.getFineAmount());
            stmt.setString(10, loan.getNotes());
            stmt.setTimestamp(11, Timestamp.valueOf(loan.getCreatedAt()));
            stmt.setTimestamp(12, Timestamp.valueOf(loan.getUpdatedAt()));

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        loan.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Préstamo creado exitosamente: " + loan.getLoanId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al crear préstamo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un préstamo por ID
     */
    public Loan findById(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar préstamo por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un préstamo por loan_id
     */
    public Loan findByLoanId(String loanId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_LOAN_ID)) {
            
            stmt.setString(1, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar préstamo por loan_id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los préstamos activos
     */
    public List<Loan> findActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE_LOANS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener préstamos activos: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Obtiene préstamos vencidos
     */
    public List<Loan> findOverdueLoans() {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_OVERDUE_LOANS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener préstamos vencidos: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Obtiene préstamos de un miembro
     */
    public List<Loan> findByMember(int memberId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MEMBER)) {
            
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener préstamos del miembro: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Obtiene préstamos activos de un miembro
     */
    public List<Loan> findActiveByMember(int memberId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE_BY_MEMBER)) {
            
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener préstamos activos del miembro: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Obtiene préstamos de un libro
     */
    public List<Loan> findByBook(int bookId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_BOOK)) {
            
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener préstamos del libro: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Busca préstamos por varios criterios
     */
    public List<Loan> search(String searchTerm) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_LOANS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            stmt.setString(6, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar préstamos: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Actualiza un préstamo
     */
    public boolean update(Loan loan) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_LOAN)) {
            
            stmt.setObject(1, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            stmt.setString(2, loan.getStatus());
            stmt.setBigDecimal(3, loan.getFineAmount());
            stmt.setString(4, loan.getNotes());
            stmt.setTimestamp(5, Timestamp.valueOf(loan.getUpdatedAt()));
            stmt.setInt(6, loan.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Préstamo actualizado exitosamente: " + loan.getLoanId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar préstamo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza el estado de un préstamo
     */
    public boolean updateStatus(int loanId, String status) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS)) {
            
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, loanId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Estado del préstamo actualizado: " + loanId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar estado del préstamo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza la multa de un préstamo
     */
    public boolean updateFine(int loanId, BigDecimal fineAmount) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_FINE)) {
            
            stmt.setBigDecimal(1, fineAmount);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, loanId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Multa actualizada para préstamo: " + loanId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar multa: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un préstamo
     */
    public boolean delete(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_LOAN)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Préstamo eliminado exitosamente ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar préstamo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Loan
     */
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setLoanId(rs.getString("loan_id"));
        loan.setBookId(rs.getInt("book_id"));
        loan.setMemberId(rs.getInt("member_id"));
        loan.setUserId(rs.getInt("user_id"));
        loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
        loan.setDueDate(rs.getDate("due_date").toLocalDate());
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            loan.setReturnDate(returnDate.toLocalDate());
        }
        
        loan.setStatus(rs.getString("status"));
        loan.setFineAmount(rs.getBigDecimal("fine_amount"));
        loan.setNotes(rs.getString("notes"));
        loan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        loan.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return loan;
    }
}
