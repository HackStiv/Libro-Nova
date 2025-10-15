package com.libronova.dao;

import com.libronova.config.DatabaseConfig;
import com.libronova.model.Member;
import com.libronova.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de miembros
 */
public class MemberDAO {
    private static final Logger logger = Logger.getInstance();

    // Consultas SQL
    private static final String INSERT_MEMBER = 
        "INSERT INTO members (member_id, first_name, last_name, email, phone, address, " +
        "birth_date, registration_date, membership_type, active, max_loans, current_loans) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID = 
        "SELECT * FROM members WHERE id = ?";

    private static final String SELECT_BY_MEMBER_ID = 
        "SELECT * FROM members WHERE member_id = ?";

    private static final String SELECT_ALL = 
        "SELECT * FROM members WHERE active = true ORDER BY last_name, first_name";

    private static final String SELECT_ACTIVE = 
        "SELECT * FROM members WHERE active = true AND current_loans < max_loans ORDER BY last_name, first_name";

    private static final String UPDATE_MEMBER = 
        "UPDATE members SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, " +
        "birth_date = ?, membership_type = ?, active = ?, max_loans = ? WHERE id = ?";

    private static final String UPDATE_LOANS = 
        "UPDATE members SET current_loans = ? WHERE id = ?";

    private static final String DELETE_MEMBER = 
        "UPDATE members SET active = false WHERE id = ?";

    private static final String SEARCH_MEMBERS = 
        "SELECT * FROM members WHERE active = true AND " +
        "(first_name LIKE ? OR last_name LIKE ? OR member_id LIKE ? OR email LIKE ?) " +
        "ORDER BY last_name, first_name";

    /**
     * Crea un nuevo miembro
     */
    public boolean create(Member member) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setString(6, member.getAddress());
            stmt.setDate(7, Date.valueOf(member.getBirthDate()));
            stmt.setDate(8, Date.valueOf(member.getRegistrationDate()));
            stmt.setString(9, member.getMembershipType());
            stmt.setBoolean(10, member.isActive());
            stmt.setInt(11, member.getMaxLoans());
            stmt.setInt(12, member.getCurrentLoans());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        member.setId(generatedKeys.getInt(1));
                    }
                }
                logger.info("Miembro creado exitosamente: " + member.getMemberId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al crear miembro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un miembro por ID
     */
    public Member findById(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar miembro por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un miembro por member_id
     */
    public Member findByMemberId(String memberId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MEMBER_ID)) {
            
            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar miembro por member_id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los miembros activos
     */
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener todos los miembros: " + e.getMessage());
        }
        return members;
    }

    /**
     * Obtiene miembros que pueden realizar préstamos
     */
    public List<Member> findActiveMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVE);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener miembros activos: " + e.getMessage());
        }
        return members;
    }

    /**
     * Busca miembros por nombre, apellido, member_id o email
     */
    public List<Member> search(String searchTerm) {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_MEMBERS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToMember(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar miembros: " + e.getMessage());
        }
        return members;
    }

    /**
     * Actualiza un miembro
     */
    public boolean update(Member member) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MEMBER)) {
            
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getAddress());
            stmt.setDate(6, Date.valueOf(member.getBirthDate()));
            stmt.setString(7, member.getMembershipType());
            stmt.setBoolean(8, member.isActive());
            stmt.setInt(9, member.getMaxLoans());
            stmt.setInt(10, member.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Miembro actualizado exitosamente: " + member.getMemberId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar miembro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza el número de préstamos actuales de un miembro
     */
    public boolean updateCurrentLoans(int memberId, int currentLoans) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_LOANS)) {
            
            stmt.setInt(1, currentLoans);
            stmt.setInt(2, memberId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Préstamos actualizados para miembro ID: " + memberId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar préstamos del miembro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina (desactiva) un miembro
     */
    public boolean delete(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MEMBER)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Miembro eliminado exitosamente ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar miembro: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mapea un ResultSet a un objeto Member
     */
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setMemberId(rs.getString("member_id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setAddress(rs.getString("address"));
        member.setBirthDate(rs.getDate("birth_date").toLocalDate());
        member.setRegistrationDate(rs.getDate("registration_date").toLocalDate());
        member.setMembershipType(rs.getString("membership_type"));
        member.setActive(rs.getBoolean("active"));
        member.setMaxLoans(rs.getInt("max_loans"));
        member.setCurrentLoans(rs.getInt("current_loans"));
        return member;
    }
}
