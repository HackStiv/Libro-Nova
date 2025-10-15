package com.libronova.model;

import java.time.LocalDate;

/**
 * Clase que representa un libro en el sistema LibroNova
 */
public class Book {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private String category;
    private int stock;
    private int availableStock;
    private boolean active;

    // Constructores
    public Book() {}

    public Book(String isbn, String title, String author, String publisher, 
                LocalDate publicationDate, String category, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.category = category;
        this.stock = stock;
        this.availableStock = stock;
        this.active = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Verifica si hay stock disponible para préstamo
     */
    public boolean isAvailable() {
        return active && availableStock > 0;
    }

    /**
     * Reduce el stock disponible al realizar un préstamo
     */
    public void borrowBook() {
        if (availableStock <= 0) {
            throw new IllegalStateException("No hay stock disponible para préstamo");
        }
        availableStock--;
    }

    /**
     * Aumenta el stock disponible al devolver un libro
     */
    public void returnBook() {
        if (availableStock >= stock) {
            throw new IllegalStateException("El stock disponible ya está al máximo");
        }
        availableStock++;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationDate=" + publicationDate +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", availableStock=" + availableStock +
                ", active=" + active +
                '}';
    }
}
