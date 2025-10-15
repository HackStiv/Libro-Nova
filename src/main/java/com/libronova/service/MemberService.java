package com.libronova.service;

import com.libronova.dao.MemberDAO;
import com.libronova.exception.MemberNotFoundException;
import com.libronova.exception.MemberLimitExceededException;
import com.libronova.model.Member;
import com.libronova.util.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Servicio para la gestión de miembros con validaciones de negocio
 */
public class MemberService {
    private final MemberDAO memberDAO;
    private static final Logger logger = Logger.getInstance();

    public MemberService() {
        this.memberDAO = new MemberDAO();
    }

    /**
     * Crea un nuevo miembro con validaciones
     */
    public boolean createMember(Member member) {
        try {
            // Validar que el member_id sea único
            if (memberDAO.findByMemberId(member.getMemberId()) != null) {
                throw new IllegalArgumentException("El ID de miembro ya existe en el sistema");
            }

            // Validar datos del miembro
            validateMemberData(member);

            boolean result = memberDAO.create(member);
            if (result) {
                logger.info("Miembro creado exitosamente: " + member.getMemberId());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al crear miembro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Busca un miembro por ID
     */
    public Member findMemberById(int id) throws MemberNotFoundException {
        Member member = memberDAO.findById(id);
        if (member == null) {
            throw new MemberNotFoundException("Miembro no encontrado con ID: " + id);
        }
        return member;
    }

    /**
     * Busca un miembro por member_id
     */
    public Member findMemberByMemberId(String memberId) throws MemberNotFoundException {
        Member member = memberDAO.findByMemberId(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Miembro no encontrado con ID: " + memberId);
        }
        return member;
    }

    /**
     * Obtiene todos los miembros
     */
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    /**
     * Obtiene miembros que pueden realizar préstamos
     */
    public List<Member> getActiveMembers() {
        return memberDAO.findActiveMembers();
    }

    /**
     * Busca miembros por término de búsqueda
     */
    public List<Member> searchMembers(String searchTerm) {
        return memberDAO.search(searchTerm);
    }

    /**
     * Actualiza un miembro
     */
    public boolean updateMember(Member member) throws MemberNotFoundException {
        try {
            // Validar que el miembro existe
            Member existingMember = memberDAO.findById(member.getId());
            if (existingMember == null) {
                throw new MemberNotFoundException("Miembro no encontrado con ID: " + member.getId());
            }

            // Validar datos del miembro
            validateMemberData(member);

            // Validar que el member_id sea único (si cambió)
            if (!existingMember.getMemberId().equals(member.getMemberId())) {
                if (memberDAO.findByMemberId(member.getMemberId()) != null) {
                    throw new IllegalArgumentException("El ID de miembro ya existe en el sistema");
                }
            }

            boolean result = memberDAO.update(member);
            if (result) {
                logger.info("Miembro actualizado exitosamente: " + member.getMemberId());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al actualizar miembro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Elimina un miembro (lo desactiva)
     */
    public boolean deleteMember(int id) throws MemberNotFoundException {
        try {
            Member member = memberDAO.findById(id);
            if (member == null) {
                throw new MemberNotFoundException("Miembro no encontrado con ID: " + id);
            }

            // Verificar que no tenga préstamos activos
            if (member.getCurrentLoans() > 0) {
                throw new IllegalStateException("No se puede eliminar un miembro con préstamos activos");
            }

            boolean result = memberDAO.delete(id);
            if (result) {
                logger.info("Miembro eliminado exitosamente: " + member.getMemberId());
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al eliminar miembro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifica si un miembro puede realizar préstamos
     */
    public boolean canMemberBorrow(int memberId) {
        Member member = memberDAO.findById(memberId);
        return member != null && member.canBorrow();
    }

    /**
     * Verifica si un miembro puede realizar un préstamo específico
     */
    public void validateMemberCanBorrow(int memberId) throws MemberLimitExceededException, MemberNotFoundException {
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            throw new MemberNotFoundException("Miembro no encontrado con ID: " + memberId);
        }

        if (!member.isActive()) {
            throw new MemberLimitExceededException("El miembro no está activo");
        }

        if (!member.canBorrow()) {
            throw new MemberLimitExceededException(
                String.format("El miembro ha alcanzado su límite de préstamos. Actual: %d, Máximo: %d",
                    member.getCurrentLoans(), member.getMaxLoans())
            );
        }
    }

    /**
     * Incrementa el contador de préstamos de un miembro
     */
    public boolean incrementMemberLoans(int memberId) throws MemberNotFoundException {
        try {
            Member member = memberDAO.findById(memberId);
            if (member == null) {
                throw new MemberNotFoundException("Miembro no encontrado con ID: " + memberId);
            }

            member.incrementLoans();
            boolean result = memberDAO.updateCurrentLoans(memberId, member.getCurrentLoans());
            
            if (result) {
                logger.info("Préstamos incrementados para miembro ID: " + memberId);
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al incrementar préstamos del miembro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Decrementa el contador de préstamos de un miembro
     */
    public boolean decrementMemberLoans(int memberId) throws MemberNotFoundException {
        try {
            Member member = memberDAO.findById(memberId);
            if (member == null) {
                throw new MemberNotFoundException("Miembro no encontrado con ID: " + memberId);
            }

            member.decrementLoans();
            boolean result = memberDAO.updateCurrentLoans(memberId, member.getCurrentLoans());
            
            if (result) {
                logger.info("Préstamos decrementados para miembro ID: " + memberId);
            }
            return result;
        } catch (Exception e) {
            logger.error("Error al decrementar préstamos del miembro: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Obtiene la edad de un miembro
     */
    public int getMemberAge(Member member) {
        return Period.between(member.getBirthDate(), LocalDate.now()).getYears();
    }

    /**
     * Valida los datos de un miembro
     */
    private void validateMemberData(Member member) {
        if (member.getMemberId() == null || member.getMemberId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de miembro es obligatorio");
        }

        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (!isValidEmail(member.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }

        if (member.getAddress() == null || member.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }

        if (member.getBirthDate() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }

        if (member.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }

        // Validar edad mínima (16 años)
        int age = getMemberAge(member);
        if (age < 16) {
            throw new IllegalArgumentException("La edad mínima para ser miembro es 16 años");
        }

        if (member.getMembershipType() == null || member.getMembershipType().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de membresía es obligatorio");
        }

        if (!isValidMembershipType(member.getMembershipType())) {
            throw new IllegalArgumentException("Tipo de membresía no válido. Valores permitidos: REGULAR, PREMIUM, VIP");
        }
    }

    /**
     * Valida el formato del email
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Valida el tipo de membresía
     */
    private boolean isValidMembershipType(String membershipType) {
        return "REGULAR".equals(membershipType) || 
               "PREMIUM".equals(membershipType) || 
               "VIP".equals(membershipType);
    }
}
