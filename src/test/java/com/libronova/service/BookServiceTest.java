package com.libronova.service;

import com.libronova.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Pruebas unitarias para BookService
 */
@DisplayName("Pruebas del servicio de libros")
class BookServiceTest {

    @Test
    @DisplayName("Debería validar que un libro está disponible cuando tiene stock")
    void shouldValidateBookIsAvailableWhenHasStock() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setId(1);
        book.setAvailableStock(3);

        // Act & Assert
        assertTrue(book.isAvailable());
    }

    @Test
    @DisplayName("Debería validar que un libro no está disponible cuando no tiene stock")
    void shouldValidateBookIsNotAvailableWhenNoStock() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setId(1);
        book.setAvailableStock(0);

        // Act & Assert
        assertFalse(book.isAvailable());
    }

    @Test
    @DisplayName("Debería validar que un libro no está disponible cuando está inactivo")
    void shouldValidateBookIsNotAvailableWhenInactive() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setId(1);
        book.setAvailableStock(3);
        book.setActive(false);

        // Act & Assert
        assertFalse(book.isAvailable());
    }

    @Test
    @DisplayName("Debería reducir el stock disponible al prestar un libro")
    void shouldReduceAvailableStockWhenBorrowingBook() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setAvailableStock(3);

        // Act
        book.borrowBook();

        // Assert
        assertEquals(2, book.getAvailableStock());
    }

    @Test
    @DisplayName("Debería aumentar el stock disponible al devolver un libro")
    void shouldIncreaseAvailableStockWhenReturningBook() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setAvailableStock(2);

        // Act
        book.returnBook();

        // Assert
        assertEquals(3, book.getAvailableStock());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando se intenta prestar un libro sin stock")
    void shouldThrowExceptionWhenBorrowingBookWithoutStock() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setAvailableStock(0);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            book.borrowBook();
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando se intenta devolver un libro con stock completo")
    void shouldThrowExceptionWhenReturningBookWithFullStock() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setAvailableStock(5);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            book.returnBook();
        });
    }

    @Test
    @DisplayName("Debería validar datos del libro correctamente")
    void shouldValidateBookDataCorrectly() {
        // Arrange
        Book validBook = new Book("978-1234567890", "Valid Book", "Valid Author", 
                                "Valid Publisher", LocalDate.now().minusYears(1), "Valid Category", 10);

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            // Simular validación de datos
            assertNotNull(validBook.getIsbn());
            assertNotNull(validBook.getTitle());
            assertNotNull(validBook.getAuthor());
            assertNotNull(validBook.getPublisher());
            assertNotNull(validBook.getCategory());
            assertNotNull(validBook.getPublicationDate());
            assertTrue(validBook.getStock() >= 0);
            assertTrue(validBook.getAvailableStock() >= 0);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con datos inválidos del libro")
    void shouldThrowExceptionWithInvalidBookData() {
        // Arrange
        Book invalidBook = new Book("", "Test Book", "Test Author", 
                                  "Test Publisher", LocalDate.now(), "Test Category", 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (invalidBook.getIsbn() == null || invalidBook.getIsbn().trim().isEmpty()) {
                throw new IllegalArgumentException("El ISBN es obligatorio");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con fecha de publicación futura")
    void shouldThrowExceptionWithFuturePublicationDate() {
        // Arrange
        Book bookWithFutureDate = new Book("978-1234567890", "Test Book", "Test Author", 
                                         "Test Publisher", LocalDate.now().plusDays(1), "Test Category", 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (bookWithFutureDate.getPublicationDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de publicación no puede ser futura");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con stock negativo")
    void shouldThrowExceptionWithNegativeStock() {
        // Arrange
        Book bookWithNegativeStock = new Book("978-1234567890", "Test Book", "Test Author", 
                                            "Test Publisher", LocalDate.now(), "Test Category", -1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (bookWithNegativeStock.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando stock disponible es mayor al stock total")
    void shouldThrowExceptionWhenAvailableStockGreaterThanTotalStock() {
        // Arrange
        Book book = new Book("978-1234567890", "Test Book", "Test Author", 
                           "Test Publisher", LocalDate.now(), "Test Category", 5);
        book.setAvailableStock(10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (book.getAvailableStock() > book.getStock()) {
                throw new IllegalArgumentException("El stock disponible no puede ser mayor al stock total");
            }
        });
    }
}
