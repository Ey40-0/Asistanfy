package models;

import controllers.MainCllr;
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
        String sql = "SELECT * FROM matter WHERE is_active = 1";
        
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
       String checkQuery = "SELECT COUNT(id) FROM matter WHERE name = ? AND is_active = 1";
       String insertQuery = "INSERT INTO matter (name, is_active) VALUES (?, ?)";
       
       try (Connection con = new connect().getConectar()) {
            // 1. Verificar si el curso ya existe
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, mat.getNombre());
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // El curso ya existe, salir del método
                        MainCllr.mostrarAlerta("Error al insertar", "El curso ya existe, no se puede añadir de nuevo.");
                        return false; // Importante: salir del método aquí
                    }
                }
            }

            // 2. Insertar el curso si no existe
            try (PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, mat.getNombre());
                ps.setInt(2, 1);
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
   
   public static void deleteMatter(int idMatter) {
        try (Connection con = new connect().getConectar()) {
            String query = "UPDATE matter SET is_active = 0 WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idMatter);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
   public static boolean updateMatter(Matter mat) {
       try (Connection con = new connect().getConectar()) {
            String query = "UPDATE matter SET name = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, mat.getNombre());
                ps.setInt(2, mat.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Materia actualizada correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró la materia con ID: " + mat.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return false;
   }
}
