package com.libronova.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase que representa un préstamo de libro en el sistema LibroNova
 */
public class Loan {
    private int id;
    private String loanId; // ID único del préstamo
    private int bookId;
    private int memberId;
    private int userId; // Usuario que procesó el préstamo
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // ACTIVE, RETURNED, OVERDUE
    private BigDecimal fineAmount;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructores
    public Loan() {}

    public Loan(String loanId, int bookId, int memberId, int userId, 
                LocalDate loanDate, LocalDate dueDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = "ACTIVE";
        this.fineAmount = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Verifica si el préstamo está activo
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    /**
     * Verifica si el préstamo está vencido
     */
    public boolean isOverdue() {
        return "OVERDUE".equals(status) || (isActive() && LocalDate.now().isAfter(dueDate));
    }

    /**
     * Verifica si el préstamo ha sido devuelto
     */
    public boolean isReturned() {
        return "RETURNED".equals(status);
    }

    /**
     * Marca el préstamo como devuelto
     */
    public void markAsReturned() {
        this.status = "RETURNED";
        this.returnDate = LocalDate.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca el préstamo como vencido
     */
    public void markAsOverdue() {
        this.status = "OVERDUE";
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calcula los días de retraso
     */
    public int getDaysOverdue() {
        if (isOverdue()) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    /**
     * Calcula la multa basada en los días de retraso
     */
    public BigDecimal calculateFine(BigDecimal dailyFineRate) {
        if (isOverdue()) {
            int daysOverdue = getDaysOverdue();
            return dailyFineRate.multiply(BigDecimal.valueOf(daysOverdue));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", loanId='" + loanId + '\'' +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", userId=" + userId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", fineAmount=" + fineAmount +
                ", notes='" + notes + '\'' +
                '}';
    }
}
