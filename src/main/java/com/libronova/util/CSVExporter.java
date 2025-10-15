package com.libronova.util;

import com.libronova.model.Book;
import com.libronova.model.Loan;
import com.libronova.model.Member;
import com.libronova.config.DatabaseConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Clase utilitaria para exportar datos a formato CSV
 */
public class CSVExporter {
    private static final Logger logger = Logger.getInstance();
    private static final String CSV_DELIMITER = DatabaseConfig.getProperty("report.csv.delimiter", ",");
    private static final String EXPORT_PATH = DatabaseConfig.getProperty("report.export.path", "./reports/");

    /**
     * Exporta el catálogo de libros a CSV
     */
    public static boolean exportBookCatalog(List<Book> books, String filename) {
        try {
            String filepath = EXPORT_PATH + filename;
            createDirectoryIfNotExists();

            try (FileWriter writer = new FileWriter(filepath)) {
                // Escribir encabezados
                writer.append("ID,ISBN,Título,Autor,Editorial,Fecha Publicación,Categoría,Stock Total,Stock Disponible,Estado\n");

                // Escribir datos
                for (Book book : books) {
                    writer.append(String.valueOf(book.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getIsbn())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getTitle())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getAuthor())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getPublisher())).append(CSV_DELIMITER);
                    writer.append(book.getPublicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getCategory())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(book.getStock())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(book.getAvailableStock())).append(CSV_DELIMITER);
                    writer.append(book.isActive() ? "Activo" : "Inactivo").append("\n");
                }

                writer.flush();
                logger.info("Catálogo de libros exportado exitosamente: " + filepath);
                return true;
            }

        } catch (IOException e) {
            logger.error("Error al exportar catálogo de libros: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Exporta los préstamos a CSV
     */
    public static boolean exportLoans(List<Loan> loans, String filename) {
        try {
            String filepath = EXPORT_PATH + filename;
            createDirectoryIfNotExists();

            try (FileWriter writer = new FileWriter(filepath)) {
                // Escribir encabezados
                writer.append("ID,ID Préstamo,ID Libro,ID Miembro,ID Usuario,Fecha Préstamo,Fecha Vencimiento,Fecha Devolución,Estado,Multa,Notas\n");

                // Escribir datos
                for (Loan loan : loans) {
                    writer.append(String.valueOf(loan.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(loan.getLoanId())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(loan.getBookId())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(loan.getMemberId())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(loan.getUserId())).append(CSV_DELIMITER);
                    writer.append(loan.getLoanDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    writer.append(loan.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    
                    if (loan.getReturnDate() != null) {
                        writer.append(loan.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    } else {
                        writer.append("");
                    }
                    writer.append(CSV_DELIMITER);
                    
                    writer.append(escapeCSV(loan.getStatus())).append(CSV_DELIMITER);
                    writer.append(loan.getFineAmount().toString()).append(CSV_DELIMITER);
                    writer.append(escapeCSV(loan.getNotes() != null ? loan.getNotes() : "")).append("\n");
                }

                writer.flush();
                logger.info("Préstamos exportados exitosamente: " + filepath);
                return true;
            }

        } catch (IOException e) {
            logger.error("Error al exportar préstamos: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Exporta los miembros a CSV
     */
    public static boolean exportMembers(List<Member> members, String filename) {
        try {
            String filepath = EXPORT_PATH + filename;
            createDirectoryIfNotExists();

            try (FileWriter writer = new FileWriter(filepath)) {
                // Escribir encabezados
                writer.append("ID,ID Miembro,Nombre,Apellido,Email,Teléfono,Dirección,Fecha Nacimiento,Fecha Registro,Tipo Membresía,Préstamos Actuales,Máximo Préstamos,Estado\n");

                // Escribir datos
                for (Member member : members) {
                    writer.append(String.valueOf(member.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getMemberId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getFirstName())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getLastName())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getEmail())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getPhone())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getAddress())).append(CSV_DELIMITER);
                    writer.append(member.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    writer.append(member.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getMembershipType())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(member.getCurrentLoans())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(member.getMaxLoans())).append(CSV_DELIMITER);
                    writer.append(member.isActive() ? "Activo" : "Inactivo").append("\n");
                }

                writer.flush();
                logger.info("Miembros exportados exitosamente: " + filepath);
                return true;
            }

        } catch (IOException e) {
            logger.error("Error al exportar miembros: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Genera un nombre de archivo con timestamp
     */
    public static String generateFilename(String prefix, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return prefix + "_" + timestamp + "." + extension;
    }

    /**
     * Escapa caracteres especiales para CSV
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // Si contiene comas, comillas o saltos de línea, encerrar en comillas
        if (value.contains(CSV_DELIMITER) || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            // Escapar comillas dobles duplicándolas
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        
        return value;
    }

    /**
     * Crea el directorio de exportación si no existe
     */
    private static void createDirectoryIfNotExists() {
        try {
            java.io.File directory = new java.io.File(EXPORT_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
                logger.info("Directorio de exportación creado: " + EXPORT_PATH);
            }
        } catch (Exception e) {
            logger.error("Error al crear directorio de exportación: " + e.getMessage(), e);
        }
    }

    /**
     * Exporta un reporte completo del sistema
     */
    public static boolean exportSystemReport(List<Book> books, List<Member> members, List<Loan> loans) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "reporte_completo_" + timestamp + ".csv";
            String filepath = EXPORT_PATH + filename;
            createDirectoryIfNotExists();

            try (FileWriter writer = new FileWriter(filepath)) {
                // Escribir encabezado del reporte
                writer.append("=== REPORTE COMPLETO DEL SISTEMA LIBRONOVA ===\n");
                writer.append("Fecha de generación: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
                writer.append("Total de libros: ").append(String.valueOf(books.size())).append("\n");
                writer.append("Total de miembros: ").append(String.valueOf(members.size())).append("\n");
                writer.append("Total de préstamos: ").append(String.valueOf(loans.size())).append("\n\n");

                // Sección de libros
                writer.append("=== CATÁLOGO DE LIBROS ===\n");
                writer.append("ID,ISBN,Título,Autor,Editorial,Categoría,Stock Total,Stock Disponible,Estado\n");
                for (Book book : books) {
                    writer.append(String.valueOf(book.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getIsbn())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getTitle())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getAuthor())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getPublisher())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(book.getCategory())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(book.getStock())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(book.getAvailableStock())).append(CSV_DELIMITER);
                    writer.append(book.isActive() ? "Activo" : "Inactivo").append("\n");
                }

                writer.append("\n=== MIEMBROS ===\n");
                writer.append("ID,ID Miembro,Nombre Completo,Email,Tipo Membresía,Préstamos Actuales,Máximo Préstamos,Estado\n");
                for (Member member : members) {
                    writer.append(String.valueOf(member.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getMemberId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getFullName())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getEmail())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(member.getMembershipType())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(member.getCurrentLoans())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(member.getMaxLoans())).append(CSV_DELIMITER);
                    writer.append(member.isActive() ? "Activo" : "Inactivo").append("\n");
                }

                writer.append("\n=== PRÉSTAMOS ===\n");
                writer.append("ID,ID Préstamo,ID Libro,ID Miembro,Fecha Préstamo,Fecha Vencimiento,Fecha Devolución,Estado,Multa\n");
                for (Loan loan : loans) {
                    writer.append(String.valueOf(loan.getId())).append(CSV_DELIMITER);
                    writer.append(escapeCSV(loan.getLoanId())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(loan.getBookId())).append(CSV_DELIMITER);
                    writer.append(String.valueOf(loan.getMemberId())).append(CSV_DELIMITER);
                    writer.append(loan.getLoanDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    writer.append(loan.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append(CSV_DELIMITER);
                    
                    if (loan.getReturnDate() != null) {
                        writer.append(loan.getReturnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    } else {
                        writer.append("");
                    }
                    writer.append(CSV_DELIMITER);
                    
                    writer.append(escapeCSV(loan.getStatus())).append(CSV_DELIMITER);
                    writer.append(loan.getFineAmount().toString()).append("\n");
                }

                writer.flush();
                logger.info("Reporte completo exportado exitosamente: " + filepath);
                return true;
            }

        } catch (IOException e) {
            logger.error("Error al exportar reporte completo: " + e.getMessage(), e);
            return false;
        }
    }
}
