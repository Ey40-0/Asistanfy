package models;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentC {
    
    public boolean insert(Student stu) {
        try (Connection con = new connect().getConectar()) {    
            String checkQuery = "SELECT COUNT(*) FROM alumnos WHERE run = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, stu.getRut());
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Ese usuario ya existe");
                        return false; // ya existe
                    }
                }
            }
            
            String query = "INSERT INTO alumnos (run, nombre, id_cur) VALUES (?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, stu.getRut());
                ps.setString(2, stu.getNombre());
                ps.setInt(3, stu.getCurso().getId());
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        stu.setId(rs.getInt(1));
                    }
                }
            }
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
