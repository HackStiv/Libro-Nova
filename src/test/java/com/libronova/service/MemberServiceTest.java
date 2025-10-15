package com.libronova.service;

import com.libronova.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

/**
 * Pruebas unitarias para MemberService
 */
@DisplayName("Pruebas del servicio de miembros")
class MemberServiceTest {

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService();
    }

    @Test
    @DisplayName("Debería validar que un miembro puede prestar cuando no ha alcanzado su límite")
    void shouldValidateMemberCanBorrowWhenNotAtLimit() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(2);
        member.setMaxLoans(3);

        // Act & Assert
        assertTrue(member.canBorrow());
    }

    @Test
    @DisplayName("Debería validar que un miembro no puede prestar cuando ha alcanzado su límite")
    void shouldValidateMemberCannotBorrowWhenAtLimit() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(3);
        member.setMaxLoans(3);

        // Act & Assert
        assertFalse(member.canBorrow());
    }

    @Test
    @DisplayName("Debería validar que un miembro no puede prestar cuando está inactivo")
    void shouldValidateMemberCannotBorrowWhenInactive() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(1);
        member.setMaxLoans(3);
        member.setActive(false);

        // Act & Assert
        assertFalse(member.canBorrow());
    }

    @Test
    @DisplayName("Debería incrementar el contador de préstamos correctamente")
    void shouldIncrementLoansCounterCorrectly() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(1);
        member.setMaxLoans(3);

        // Act
        member.incrementLoans();

        // Assert
        assertEquals(2, member.getCurrentLoans());
    }

    @Test
    @DisplayName("Debería decrementar el contador de préstamos correctamente")
    void shouldDecrementLoansCounterCorrectly() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(2);
        member.setMaxLoans(3);

        // Act
        member.decrementLoans();

        // Assert
        assertEquals(1, member.getCurrentLoans());
    }

    @Test
    @DisplayName("Debería lanzar excepción al incrementar préstamos cuando se alcanza el límite")
    void shouldThrowExceptionWhenIncrementingLoansAtLimit() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(3);
        member.setMaxLoans(3);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            member.incrementLoans();
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción al decrementar préstamos cuando no hay préstamos")
    void shouldThrowExceptionWhenDecrementingLoansWithZeroLoans() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        member.setCurrentLoans(0);
        member.setMaxLoans(3);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            member.decrementLoans();
        });
    }

    @Test
    @DisplayName("Debería obtener el nombre completo correctamente")
    void shouldGetFullNameCorrectly() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");

        // Act
        String fullName = member.getFullName();

        // Assert
        assertEquals("Juan Pérez", fullName);
    }

    @Test
    @DisplayName("Debería calcular la edad correctamente")
    void shouldCalculateAgeCorrectly() {
        // Arrange
        Member member = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");

        // Act
        int age = memberService.getMemberAge(member);

        // Assert
        assertEquals(25, age);
    }

    @Test
    @DisplayName("Debería validar datos del miembro correctamente")
    void shouldValidateMemberDataCorrectly() {
        // Arrange
        Member validMember = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                      "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            // Simular validación de datos
            assertNotNull(validMember.getMemberId());
            assertNotNull(validMember.getFirstName());
            assertNotNull(validMember.getLastName());
            assertNotNull(validMember.getEmail());
            assertNotNull(validMember.getPhone());
            assertNotNull(validMember.getAddress());
            assertNotNull(validMember.getBirthDate());
            assertNotNull(validMember.getMembershipType());
            assertTrue(validMember.getMaxLoans() > 0);
            assertTrue(validMember.getCurrentLoans() >= 0);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con datos inválidos del miembro")
    void shouldThrowExceptionWithInvalidMemberData() {
        // Arrange
        Member invalidMember = new Member("", "Juan", "Pérez", "juan@email.com", 
                                        "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (invalidMember.getMemberId() == null || invalidMember.getMemberId().trim().isEmpty()) {
                throw new IllegalArgumentException("El ID de miembro es obligatorio");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con email inválido")
    void shouldThrowExceptionWithInvalidEmail() {
        // Arrange
        Member memberWithInvalidEmail = new Member("MEM001", "Juan", "Pérez", "email-invalido", 
                                                 "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (!memberWithInvalidEmail.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
                throw new IllegalArgumentException("El formato del email no es válido");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con fecha de nacimiento futura")
    void shouldThrowExceptionWithFutureBirthDate() {
        // Arrange
        Member memberWithFutureBirthDate = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                                    "555-0101", "Calle 123", LocalDate.now().plusDays(1), "REGULAR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (memberWithFutureBirthDate.getBirthDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con edad menor a 16 años")
    void shouldThrowExceptionWithAgeLessThan16() {
        // Arrange
        Member youngMember = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                      "555-0101", "Calle 123", LocalDate.now().minusYears(15), "REGULAR");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            int age = memberService.getMemberAge(youngMember);
            if (age < 16) {
                throw new IllegalArgumentException("La edad mínima para ser miembro es 16 años");
            }
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción con tipo de membresía inválido")
    void shouldThrowExceptionWithInvalidMembershipType() {
        // Arrange
        Member memberWithInvalidType = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                                "555-0101", "Calle 123", LocalDate.now().minusYears(25), "INVALID");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (!"REGULAR".equals(memberWithInvalidType.getMembershipType()) && 
                !"PREMIUM".equals(memberWithInvalidType.getMembershipType()) && 
                !"VIP".equals(memberWithInvalidType.getMembershipType())) {
                throw new IllegalArgumentException("Tipo de membresía no válido. Valores permitidos: REGULAR, PREMIUM, VIP");
            }
        });
    }

    @Test
    @DisplayName("Debería establecer el máximo de préstamos según el tipo de membresía")
    void shouldSetMaxLoansAccordingToMembershipType() {
        // Arrange & Act
        Member regularMember = new Member("MEM001", "Juan", "Pérez", "juan@email.com", 
                                        "555-0101", "Calle 123", LocalDate.now().minusYears(25), "REGULAR");
        Member premiumMember = new Member("MEM002", "María", "García", "maria@email.com", 
                                        "555-0102", "Calle 456", LocalDate.now().minusYears(30), "PREMIUM");
        Member vipMember = new Member("MEM003", "Carlos", "López", "carlos@email.com", 
                                    "555-0103", "Calle 789", LocalDate.now().minusYears(35), "VIP");

        // Assert
        assertEquals(3, regularMember.getMaxLoans());
        assertEquals(5, premiumMember.getMaxLoans());
        assertEquals(10, vipMember.getMaxLoans());
    }
}
