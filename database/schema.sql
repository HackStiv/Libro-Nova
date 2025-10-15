-- Script de creación de la base de datos LibroNova
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS libronova CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE libronova;

-- Tabla de usuarios del sistema
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('ADMIN', 'LIBRARIAN', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Tabla de libros
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    publication_date DATE NOT NULL,
    category VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    available_stock INT NOT NULL DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_isbn (isbn),
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_category (category),
    INDEX idx_active (active),
    CHECK (stock >= 0),
    CHECK (available_stock >= 0),
    CHECK (available_stock <= stock)
);

-- Tabla de miembros
CREATE TABLE members (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    birth_date DATE NOT NULL,
    registration_date DATE NOT NULL,
    membership_type ENUM('REGULAR', 'PREMIUM', 'VIP') NOT NULL DEFAULT 'REGULAR',
    active BOOLEAN DEFAULT TRUE,
    max_loans INT NOT NULL DEFAULT 3,
    current_loans INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_member_id (member_id),
    INDEX idx_email (email),
    INDEX idx_membership_type (membership_type),
    INDEX idx_active (active),
    CHECK (max_loans > 0),
    CHECK (current_loans >= 0),
    CHECK (current_loans <= max_loans)
);

-- Tabla de préstamos
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id VARCHAR(20) NOT NULL UNIQUE,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    user_id INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') NOT NULL DEFAULT 'ACTIVE',
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_loan_id (loan_id),
    INDEX idx_book_id (book_id),
    INDEX idx_member_id (member_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_loan_date (loan_date),
    INDEX idx_due_date (due_date),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CHECK (fine_amount >= 0),
    CHECK (return_date IS NULL OR return_date >= loan_date)
);

-- Insertar datos de prueba

-- Usuario administrador por defecto
INSERT INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@libronova.com', 'ADMIN'),
('librarian1', 'lib123', 'librarian1@libronova.com', 'LIBRARIAN'),
('librarian2', 'lib456', 'librarian2@libronova.com', 'LIBRARIAN');

-- Libros de ejemplo
INSERT INTO books (isbn, title, author, publisher, publication_date, category, stock, available_stock) VALUES 
('978-84-376-0494-7', 'Cien años de soledad', 'Gabriel García Márquez', 'Cátedra', '1967-06-05', 'Literatura', 5, 5),
('978-84-376-0495-4', 'El Quijote', 'Miguel de Cervantes', 'Cátedra', '1605-01-16', 'Literatura', 3, 3),
('978-84-376-0496-1', '1984', 'George Orwell', 'Debolsillo', '1949-06-08', 'Ciencia Ficción', 4, 4),
('978-84-376-0497-8', 'El Señor de los Anillos', 'J.R.R. Tolkien', 'Minotauro', '1954-07-29', 'Fantasía', 6, 6),
('978-84-376-0498-5', 'Crimen y Castigo', 'Fiódor Dostoyevski', 'Alma', '1866-01-01', 'Literatura', 2, 2),
('978-84-376-0499-2', 'El Principito', 'Antoine de Saint-Exupéry', 'Salamandra', '1943-04-06', 'Infantil', 8, 8),
('978-84-376-0500-5', 'Harry Potter y la Piedra Filosofal', 'J.K. Rowling', 'Salamandra', '1997-06-26', 'Fantasía', 7, 7),
('978-84-376-0501-2', 'Orgullo y Prejuicio', 'Jane Austen', 'Alma', '1813-01-28', 'Romance', 3, 3),
('978-84-376-0502-9', 'Los Miserables', 'Victor Hugo', 'Alma', '1862-01-01', 'Literatura', 4, 4),
('978-84-376-0503-6', 'Don Juan Tenorio', 'José Zorrilla', 'Cátedra', '1844-03-28', 'Teatro', 2, 2);

-- Miembros de ejemplo
INSERT INTO members (member_id, first_name, last_name, email, phone, address, birth_date, registration_date, membership_type, max_loans) VALUES 
('MEM001', 'Juan', 'Pérez', 'juan.perez@email.com', '555-0101', 'Calle Mayor 123, Madrid', '1985-03-15', '2024-01-15', 'REGULAR', 3),
('MEM002', 'María', 'García', 'maria.garcia@email.com', '555-0102', 'Avenida de la Paz 456, Barcelona', '1990-07-22', '2024-01-20', 'PREMIUM', 5),
('MEM003', 'Carlos', 'López', 'carlos.lopez@email.com', '555-0103', 'Plaza España 789, Valencia', '1988-11-10', '2024-02-01', 'VIP', 10),
('MEM004', 'Ana', 'Martín', 'ana.martin@email.com', '555-0104', 'Calle Real 321, Sevilla', '1992-05-08', '2024-02-10', 'REGULAR', 3),
('MEM005', 'Luis', 'Rodríguez', 'luis.rodriguez@email.com', '555-0105', 'Gran Vía 654, Bilbao', '1987-09-14', '2024-02-15', 'PREMIUM', 5);

-- Crear algunos préstamos de ejemplo
INSERT INTO loans (loan_id, book_id, member_id, user_id, loan_date, due_date, status) VALUES 
('LOAN-ABC12345', 1, 1, 2, '2024-03-01', '2024-03-15', 'ACTIVE'),
('LOAN-DEF67890', 3, 2, 2, '2024-03-05', '2024-03-19', 'ACTIVE'),
('LOAN-GHI11111', 5, 3, 3, '2024-02-20', '2024-03-05', 'OVERDUE'),
('LOAN-JKL22222', 7, 4, 2, '2024-03-10', '2024-03-24', 'ACTIVE');

-- Actualizar contadores de préstamos de los miembros
UPDATE members SET current_loans = 1 WHERE id = 1;
UPDATE members SET current_loans = 1 WHERE id = 2;
UPDATE members SET current_loans = 1 WHERE id = 3;
UPDATE members SET current_loans = 1 WHERE id = 4;

-- Actualizar stock disponible de los libros prestados
UPDATE books SET available_stock = 4 WHERE id = 1;
UPDATE books SET available_stock = 3 WHERE id = 3;
UPDATE books SET available_stock = 1 WHERE id = 5;
UPDATE books SET available_stock = 6 WHERE id = 7;

-- Crear vistas útiles

-- Vista de préstamos activos con información detallada
CREATE VIEW v_active_loans AS
SELECT 
    l.id,
    l.loan_id,
    b.title as book_title,
    b.isbn,
    CONCAT(m.first_name, ' ', m.last_name) as member_name,
    m.member_id,
    l.loan_date,
    l.due_date,
    DATEDIFF(CURDATE(), l.due_date) as days_overdue,
    l.fine_amount
FROM loans l
JOIN books b ON l.book_id = b.id
JOIN members m ON l.member_id = m.id
WHERE l.status = 'ACTIVE';

-- Vista de libros disponibles
CREATE VIEW v_available_books AS
SELECT 
    id,
    isbn,
    title,
    author,
    publisher,
    category,
    available_stock,
    stock
FROM books
WHERE active = TRUE AND available_stock > 0
ORDER BY title;

-- Vista de miembros activos
CREATE VIEW v_active_members AS
SELECT 
    id,
    member_id,
    CONCAT(first_name, ' ', last_name) as full_name,
    email,
    phone,
    membership_type,
    current_loans,
    max_loans,
    (max_loans - current_loans) as available_loans
FROM members
WHERE active = TRUE
ORDER BY last_name, first_name;

-- Crear procedimientos almacenados

-- Procedimiento para actualizar préstamos vencidos
DELIMITER //
CREATE PROCEDURE UpdateOverdueLoans()
BEGIN
    UPDATE loans 
    SET status = 'OVERDUE', updated_at = CURRENT_TIMESTAMP
    WHERE status = 'ACTIVE' 
    AND due_date < CURDATE();
END //
DELIMITER ;

-- Procedimiento para calcular multas
DELIMITER //
CREATE PROCEDURE CalculateFines(IN daily_rate DECIMAL(10,2))
BEGIN
    UPDATE loans 
    SET fine_amount = GREATEST(0, DATEDIFF(CURDATE(), due_date)) * daily_rate,
        updated_at = CURRENT_TIMESTAMP
    WHERE status = 'OVERDUE' 
    AND due_date < CURDATE();
END //
DELIMITER ;

-- Crear triggers

-- Trigger para actualizar available_stock cuando se crea un préstamo
DELIMITER //
CREATE TRIGGER tr_loan_created
AFTER INSERT ON loans
FOR EACH ROW
BEGIN
    UPDATE books 
    SET available_stock = available_stock - 1,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.book_id;
    
    UPDATE members 
    SET current_loans = current_loans + 1,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.member_id;
END //
DELIMITER ;

-- Trigger para actualizar available_stock cuando se devuelve un libro
DELIMITER //
CREATE TRIGGER tr_loan_returned
AFTER UPDATE ON loans
FOR EACH ROW
BEGIN
    IF OLD.status = 'ACTIVE' AND NEW.status = 'RETURNED' THEN
        UPDATE books 
        SET available_stock = available_stock + 1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = NEW.book_id;
        
        UPDATE members 
        SET current_loans = current_loans - 1,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = NEW.member_id;
    END IF;
END //
DELIMITER ;

-- Crear índices adicionales para optimizar consultas
CREATE INDEX idx_loans_due_date_status ON loans(due_date, status);
CREATE INDEX idx_books_category_active ON books(category, active);
CREATE INDEX idx_members_membership_active ON members(membership_type, active);

-- Mostrar información de la base de datos creada
SELECT 'Base de datos LibroNova creada exitosamente' as message;
SELECT COUNT(*) as total_books FROM books;
SELECT COUNT(*) as total_members FROM members;
SELECT COUNT(*) as total_loans FROM loans;
SELECT COUNT(*) as total_users FROM users;
