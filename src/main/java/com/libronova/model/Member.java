package com.libronova.model;

import java.time.LocalDate;

/**
 * Clase que representa un miembro de la biblioteca LibroNova
 */
public class Member {
    private int id;
    private String memberId; // ID único del miembro
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private String membershipType; // REGULAR, PREMIUM, VIP
    private boolean active;
    private int maxLoans; // Máximo de préstamos simultáneos
    private int currentLoans; // Préstamos actuales

    // Constructores
    public Member() {}

    public Member(String memberId, String firstName, String lastName, String email, 
                  String phone, String address, LocalDate birthDate, String membershipType) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
        this.registrationDate = LocalDate.now();
        this.membershipType = membershipType;
        this.active = true;
        this.maxLoans = getMaxLoansByType(membershipType);
        this.currentLoans = 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
        this.maxLoans = getMaxLoansByType(membershipType);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public void setMaxLoans(int maxLoans) {
        this.maxLoans = maxLoans;
    }

    public int getCurrentLoans() {
        return currentLoans;
    }

    public void setCurrentLoans(int currentLoans) {
        this.currentLoans = currentLoans;
    }

    /**
     * Obtiene el nombre completo del miembro
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Verifica si el miembro puede realizar más préstamos
     */
    public boolean canBorrow() {
        return active && currentLoans < maxLoans;
    }

    /**
     * Incrementa el contador de préstamos actuales
     */
    public void incrementLoans() {
        if (currentLoans >= maxLoans) {
            throw new IllegalStateException("El miembro ha alcanzado su límite máximo de préstamos");
        }
        currentLoans++;
    }

    /**
     * Decrementa el contador de préstamos actuales
     */
    public void decrementLoans() {
        if (currentLoans <= 0) {
            throw new IllegalStateException("El miembro no tiene préstamos para decrementar");
        }
        currentLoans--;
    }

    /**
     * Obtiene el máximo de préstamos según el tipo de membresía
     */
    private int getMaxLoansByType(String membershipType) {
        switch (membershipType.toUpperCase()) {
            case "REGULAR":
                return 3;
            case "PREMIUM":
                return 5;
            case "VIP":
                return 10;
            default:
                return 3;
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", membershipType='" + membershipType + '\'' +
                ", active=" + active +
                ", maxLoans=" + maxLoans +
                ", currentLoans=" + currentLoans +
                '}';
    }
}
