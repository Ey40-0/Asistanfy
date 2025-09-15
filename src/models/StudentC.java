package models;

import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.Connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentC {
    
    public boolean insert(Student stu) {
        String query = "INSERT INTO alumnos (run, nombre, id_cur) VALUES (?,?,?)";
        try (Connection con = new Connect().getConectar();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, stu.getRut());
            ps.setString(2, stu.getNombre());
            ps.setInt(3, stu.getCurso().getId());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        stu.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
}
