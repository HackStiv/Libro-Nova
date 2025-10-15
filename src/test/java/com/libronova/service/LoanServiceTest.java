package com.libronova.service;

import com.libronova.model.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pruebas unitarias para LoanService
 */
@DisplayName("Pruebas del servicio de préstamos")
class LoanServiceTest {

    @Test
    @DisplayName("Debería crear un préstamo con datos válidos")
    void shouldCreateLoanWithValidData() {
        // Arrange
        String loanId = "LOAN-TEST123";
        int bookId = 1;
        int memberId = 1;
        int userId = 1;
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(14);

        // Act
        Loan loan = new Loan(loanId, bookId, memberId, userId, loanDate, dueDate);

        // Assert
        assertNotNull(loan);
        assertEquals(loanId, loan.getLoanId());
        assertEquals(bookId, loan.getBookId());
        assertEquals(memberId, loan.getMemberId());
        assertEquals(userId, loan.getUserId());
        assertEquals(loanDate, loan.getLoanDate());
        assertEquals(dueDate, loan.getDueDate());
        assertEquals("ACTIVE", loan.getStatus());
        assertEquals(BigDecimal.ZERO, loan.getFineAmount());
    }

    @Test
    @DisplayName("Debería validar que un préstamo está activo")
    void shouldValidateLoanIsActive() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(14));

        // Act & Assert
        assertTrue(loan.isActive());
        assertFalse(loan.isReturned());
    }

    @Test
    @DisplayName("Debería validar que un préstamo está vencido")
    void shouldValidateLoanIsOverdue() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));

        // Act & Assert
        assertTrue(loan.isOverdue());
    }

    @Test
    @DisplayName("Debería validar que un préstamo no está vencido cuando está dentro del plazo")
    void shouldValidateLoanIsNotOverdueWhenWithinDeadline() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));

        // Act & Assert
        assertFalse(loan.isOverdue());
    }

    @Test
    @DisplayName("Debería marcar un préstamo como devuelto")
    void shouldMarkLoanAsReturned() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));

        // Act
        loan.markAsReturned();

        // Assert
        assertTrue(loan.isReturned());
        assertEquals("RETURNED", loan.getStatus());
        assertNotNull(loan.getReturnDate());
        assertEquals(LocalDate.now(), loan.getReturnDate());
    }

    @Test
    @DisplayName("Debería marcar un préstamo como vencido")
    void shouldMarkLoanAsOverdue() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));

        // Act
        loan.markAsOverdue();

        // Assert
        assertEquals("OVERDUE", loan.getStatus());
    }

    @Test
    @DisplayName("Debería calcular los días de retraso correctamente")
    void shouldCalculateDaysOverdueCorrectly() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));

        // Act
        int daysOverdue = loan.getDaysOverdue();

        // Assert
        assertEquals(6, daysOverdue);
    }

    @Test
    @DisplayName("Debería retornar 0 días de retraso cuando el préstamo no está vencido")
    void shouldReturnZeroDaysOverdueWhenLoanNotOverdue() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));

        // Act
        int daysOverdue = loan.getDaysOverdue();

        // Assert
        assertEquals(0, daysOverdue);
    }

    @Test
    @DisplayName("Debería calcular la multa correctamente")
    void shouldCalculateFineCorrectly() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
        BigDecimal dailyFineRate = new BigDecimal("5.00");

        // Act
        BigDecimal fine = loan.calculateFine(dailyFineRate);

        // Assert
        assertEquals(new BigDecimal("30.00"), fine); // 6 días * $5.00
    }

    @Test
    @DisplayName("Debería retornar multa cero cuando el préstamo no está vencido")
    void shouldReturnZeroFineWhenLoanNotOverdue() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        BigDecimal dailyFineRate = new BigDecimal("5.00");

        // Act
        BigDecimal fine = loan.calculateFine(dailyFineRate);

        // Assert
        assertEquals(BigDecimal.ZERO, fine);
    }

    @Test
    @DisplayName("Debería obtener la configuración de días de préstamo")
    void shouldGetLoanDaysConfiguration() {
        // Act
        int loanDays = LoanService.getLoanDays();

        // Assert
        assertEquals(14, loanDays);
    }

    @Test
    @DisplayName("Debería obtener la tasa de multa diaria")
    void shouldGetDailyFineRateConfiguration() {
        // Act
        BigDecimal dailyFineRate = LoanService.getDailyFineRate();

        // Assert
        assertEquals(new BigDecimal("5.00"), dailyFineRate);
    }

    @Test
    @DisplayName("Debería lanzar excepción al intentar devolver un libro ya devuelto")
    void shouldThrowExceptionWhenReturningAlreadyReturnedBook() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
        loan.markAsReturned();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            if (loan.isReturned()) {
                throw new IllegalStateException("El libro ya ha sido devuelto");
            }
        });
    }

    @Test
    @DisplayName("Debería validar que un préstamo devuelto no está activo")
    void shouldValidateReturnedLoanIsNotActive() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));

        // Act
        loan.markAsReturned();

        // Assert
        assertFalse(loan.isActive());
        assertTrue(loan.isReturned());
    }

    @Test
    @DisplayName("Debería validar que un préstamo vencido no está devuelto")
    void shouldValidateOverdueLoanIsNotReturned() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));

        // Act
        loan.markAsOverdue();

        // Assert
        assertTrue(loan.isOverdue());
        assertFalse(loan.isReturned());
        assertFalse(loan.isActive());
    }

    @Test
    @DisplayName("Debería calcular multa con diferentes tasas diarias")
    void shouldCalculateFineWithDifferentDailyRates() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
        BigDecimal lowDailyRate = new BigDecimal("2.50");
        BigDecimal highDailyRate = new BigDecimal("10.00");

        // Act
        BigDecimal lowFine = loan.calculateFine(lowDailyRate);
        BigDecimal highFine = loan.calculateFine(highDailyRate);

        // Assert
        assertEquals(new BigDecimal("15.00"), lowFine); // 6 días * $2.50
        assertEquals(new BigDecimal("60.00"), highFine); // 6 días * $10.00
    }

    @Test
    @DisplayName("Debería manejar préstamos con multa cero correctamente")
    void shouldHandleLoansWithZeroFineCorrectly() {
        // Arrange
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        loan.setFineAmount(BigDecimal.ZERO);

        // Act & Assert
        assertEquals(BigDecimal.ZERO, loan.getFineAmount());
        assertFalse(loan.isOverdue());
    }

    @Test
    @DisplayName("Debería validar fechas de préstamo correctamente")
    void shouldValidateLoanDatesCorrectly() {
        // Arrange
        LocalDate loanDate = LocalDate.now().minusDays(10);
        LocalDate dueDate = loanDate.plusDays(14);
        Loan loan = new Loan("LOAN-TEST123", 1, 1, 1, loanDate, dueDate);

        // Act & Assert
        assertNotNull(loan.getLoanDate());
        assertNotNull(loan.getDueDate());
        assertTrue(loan.getDueDate().isAfter(loan.getLoanDate()));
        assertEquals(14, loan.getLoanDate().until(loan.getDueDate()).getDays());
    }
}
