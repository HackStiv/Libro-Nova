package com.libronova.service;

import com.libronova.dao.BookDAO;
import com.libronova.exception.BookNotFoundException;
import com.libronova.exception.InsufficientStockException;
import com.libronova.model.Book;
import com.libronova.util.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de libros con validaciones de negocio
 */
public class BookService {
    private final BookDAO bookDAO;
    private static final Logger logger = Logger.getInstance();

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    /**
     * Crea un nuevo libro con validaciones
     */
    public boolean createBook(Book book) {
        try {
            // Validar que el ISBN sea único
            if (bookDAO.findByIsbn(book.getIsbn()) != null) {
                throw new IllegalArgumentException("El ISBN ya existe en el sistema");
            }

            // Validar datos del libro
            validateBookData(book);

            boolean result = bookDAO.create(book);
            if (result) {
                logger.info("Libro creado exitosamente: " + book.getIsbn());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al crear libro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Busca un libro por ID
     */
    public Book findBookById(int id) throws BookNotFoundException {
        Book book = bookDAO.findById(id);
        if (book == null) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + id);
        }
        return book;
    }

    /**
     * Busca un libro por ISBN
     */
    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        Book book = bookDAO.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Libro no encontrado con ISBN: " + isbn);
        }
        return book;
    }

    /**
     * Obtiene todos los libros
     */
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    /**
     * Busca libros por categoría
     */
    public List<Book> getBooksByCategory(String category) {
        return bookDAO.findByCategory(category);
    }

    /**
     * Obtiene libros disponibles para préstamo
     */
    public List<Book> getAvailableBooks() {
        return bookDAO.findAvailable();
    }

    /**
     * Busca libros por término de búsqueda
     */
    public List<Book> searchBooks(String searchTerm) {
        return bookDAO.search(searchTerm);
    }

    /**
     * Actualiza un libro
     */
    public boolean updateBook(Book book) throws BookNotFoundException {
        try {
            // Validar que el libro existe
            Book existingBook = bookDAO.findById(book.getId());
            if (existingBook == null) {
                throw new BookNotFoundException("Libro no encontrado con ID: " + book.getId());
            }

            // Validar datos del libro
            validateBookData(book);

            // Validar que el ISBN sea único (si cambió)
            if (!existingBook.getIsbn().equals(book.getIsbn())) {
                if (bookDAO.findByIsbn(book.getIsbn()) != null) {
                    throw new IllegalArgumentException("El ISBN ya existe en el sistema");
                }
            }

            boolean result = bookDAO.update(book);
            if (result) {
                logger.info("Libro actualizado exitosamente: " + book.getIsbn());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al actualizar libro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Elimina un libro (lo desactiva)
     */
    public boolean deleteBook(int id) throws BookNotFoundException {
        Book book = bookDAO.findById(id);
        if (book == null) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + id);
        }

        boolean result = bookDAO.delete(id);
        if (result) {
            logger.info("Libro eliminado exitosamente: " + book.getIsbn());
        }
        return result;
    }

    /**
     * Verifica si un libro está disponible para préstamo
     */
    public boolean isBookAvailable(int bookId) {
        Book book = bookDAO.findById(bookId);
        return book != null && book.isAvailable();
    }

    /**
     * Verifica si hay stock suficiente para un préstamo
     */
    public void validateStockAvailability(int bookId, int quantity) throws InsufficientStockException, BookNotFoundException {
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
        }

        if (!book.isActive()) {
            throw new InsufficientStockException("El libro no está activo");
        }

        if (book.getAvailableStock() < quantity) {
            throw new InsufficientStockException(
                String.format("Stock insuficiente. Disponible: %d, Solicitado: %d", 
                    book.getAvailableStock(), quantity)
            );
        }
    }

    /**
     * Reduce el stock disponible al realizar un préstamo
     */
    public boolean borrowBook(int bookId) throws BookNotFoundException, InsufficientStockException {
        try {
            Book book = bookDAO.findById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
            }

            if (!book.isAvailable()) {
                throw new InsufficientStockException("El libro no está disponible para préstamo");
            }

            book.borrowBook();
            boolean result = bookDAO.updateStock(bookId, book.getAvailableStock());
            
            if (result) {
                logger.info("Stock reducido para libro ID: " + bookId);
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al reducir stock: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Aumenta el stock disponible al devolver un libro
     */
    public boolean returnBook(int bookId) throws BookNotFoundException {
        try {
            Book book = bookDAO.findById(bookId);
            if (book == null) {
                throw new BookNotFoundException("Libro no encontrado con ID: " + bookId);
            }

            book.returnBook();
            boolean result = bookDAO.updateStock(bookId, book.getAvailableStock());
            
            if (result) {
                logger.info("Stock aumentado para libro ID: " + bookId);
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al aumentar stock: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Valida los datos de un libro
     */
    private void validateBookData(Book book) {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN es obligatorio");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor es obligatorio");
        }

        if (book.getPublisher() == null || book.getPublisher().trim().isEmpty()) {
            throw new IllegalArgumentException("La editorial es obligatoria");
        }

        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        if (book.getPublicationDate() == null) {
            throw new IllegalArgumentException("La fecha de publicación es obligatoria");
        }

        if (book.getPublicationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser futura");
        }

        if (book.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        if (book.getAvailableStock() < 0) {
            throw new IllegalArgumentException("El stock disponible no puede ser negativo");
        }

        if (book.getAvailableStock() > book.getStock()) {
            throw new IllegalArgumentException("El stock disponible no puede ser mayor al stock total");
        }
    }
}
