package com.libronova.dao;

import com.libronova.config.DatabaseConfig;
import com.libronova.model.Book;
import com.libronova.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de libros
 */
public class BookDAO {
    private static final Logger logger = Logger.getInstance();

    // Consultas SQL
    private static final String INSERT_BOOK = 
        "INSERT INTO books (isbn, title, author, publisher, publication_date, category, stock, available_stock, active) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID = 
        "SELECT * FROM books WHERE id = ?";

    private static final String SELECT_BY_ISBN = 
        "SELECT * FROM books WHERE isbn = ?";

    private static final String SELECT_ALL = 
        "SELECT * FROM books WHERE active = true ORDER BY title";

    private static final String SELECT_BY_CATEGORY = 
        "SELECT * FROM books WHERE category = ? AND active = true ORDER BY title";

    private static final String SELECT_AVAILABLE = 
        "SELECT * FROM books WHERE active = true AND available_stock > 0 ORDER BY title";

    private static final String UPDATE_BOOK = 
        "UPDATE books SET title = ?, author = ?, publisher = ?, publication_date = ?, " +
        "category = ?, stock = ?, available_stock = ?, active = ? WHERE id = ?";

    private static final String UPDATE_STOCK = 
        "UPDATE books SET available_stock = ? WHERE id = ?";

    private static final String DELETE_BOOK = 
        "UPDATE books SET active = false WHERE id = ?";

    private static final String SEARCH_BOOKS = 
        "SELECT * FROM books WHERE active = true AND " +
        "(title LIKE ? OR author LIKE ? OR isbn LIKE ?) ORDER BY title";

    /**
     * Crea un nuevo libro
     */
    public boolean create(Book book) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getPublisher());
            stmt.setDate(5, Date.valueOf(book.getPublicationDate()));
            stmt.setString(6, book.getCategory());
            stmt.setInt(7, book.getStock());
            stmt.setInt(8, book.getAvailableStock());
            stmt.setBoolean(9, book.isActive());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Libro creado exitosamente: " + book.getIsbn());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al crear libro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un libro por ID
     */
    public Book findById(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar libro por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un libro por ISBN
     */
    public Book findByIsbn(String isbn) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ISBN)) {
            
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar libro por ISBN: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los libros activos
     */
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todos los libros: " + e.getMessage());
        }
        return books;
    }

    /**
     * Obtiene libros por categoría
     */
    public List<Book> findByCategory(String category) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CATEGORY)) {
            
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar libros por categoría: " + e.getMessage());
        }
        return books;
    }

    /**
     * Obtiene libros disponibles para préstamo
     */
    public List<Book> findAvailable() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_AVAILABLE);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener libros disponibles: " + e.getMessage());
        }
        return books;
    }

    /**
     * Busca libros por título, autor o ISBN
     */
    public List<Book> search(String searchTerm) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BOOKS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar libros: " + e.getMessage());
        }
        return books;
    }

    /**
     * Actualiza un libro
     */
    public boolean update(Book book) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_BOOK)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setDate(4, Date.valueOf(book.getPublicationDate()));
            stmt.setString(5, book.getCategory());
            stmt.setInt(6, book.getStock());
            stmt.setInt(7, book.getAvailableStock());
            stmt.setBoolean(8, book.isActive());
            stmt.setInt(9, book.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Libro actualizado exitosamente: " + book.getIsbn());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar libro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza el stock disponible de un libro
     */
    public boolean updateStock(int bookId, int availableStock) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STOCK)) {
            
            stmt.setInt(1, availableStock);
            stmt.setInt(2, bookId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Stock actualizado para libro ID: " + bookId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar stock: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina (desactiva) un libro
     */
    public boolean delete(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BOOK)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Libro eliminado exitosamente ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar libro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Book
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublicationDate(rs.getDate("publication_date").toLocalDate());
        book.setCategory(rs.getString("category"));
        book.setStock(rs.getInt("stock"));
        book.setAvailableStock(rs.getInt("available_stock"));
        book.setActive(rs.getBoolean("active"));
        return book;
    }
}
