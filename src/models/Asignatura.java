package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import proyectojavafx.connect;

public class Asignatura {
    private int id;
    private String nombre;

    public Asignatura(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    public static List<Asignatura> obtenerAsignaturas() {
        List<Asignatura> asignaturas = new ArrayList<>();
        String sql = "SELECT * FROM asignatura";
        
        try (Connection con = new connect().getConectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                asignaturas.add(new Asignatura(rs.getInt("id_asign"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asignaturas;
   }
    
}
