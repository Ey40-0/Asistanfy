package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectojavafx.connect;

public class Matter {
    private int id;
    private String nombre;

    public Matter(int id, String nombre) {
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
    
    public static ObservableList<Matter> getMatters() {
        ObservableList<Matter> matters = FXCollections.observableArrayList();
        String sql = "SELECT * FROM matter";
        
        try (Connection con = new connect().getConectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                matters.add(new Matter(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matters;
   }
    
   public static boolean addNewMatter(Matter mat) {
       String checkQuery = "SELECT COUNT(id) FROM matter WHERE name = ?";
       String insertQuery = "INSERT INTO matter (name) VALUES (?)";
       
       try (Connection con = new connect().getConectar()) {
            // 1. Verificar si el curso ya existe
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, mat.getNombre());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // El curso ya existe, salir del método
                        System.out.println("El curso ya existe, no se puede añadir de nuevo.");
                        return false; // Importante: salir del método aquí
                    }
                }
            }

            // 2. Insertar el curso si no existe
            try (PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, mat.getNombre());
                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            mat.setId(rs.getInt(1));
                            System.out.println("Curso añadido con éxito con el ID: " + mat.getId());
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
       return false;
    }
}
