package models;

public class Sesion {
    private static Sesion instancia;
    private int id;
    private int id_rol;

    public Sesion() {
        
    }

    public Sesion(int id, int id_rol) {
        this.id = id;
        this.id_rol = id_rol;
    }
    
    public static void iniciarSesion(int id, int id_rol) {
        // Siempre crea una nueva sesión, reemplazando la anterior
        instancia = new Sesion(id, id_rol);
    }

    public static Sesion getInstance() {
        return instancia;
    }

    public int getId() {
        return id;
    }

    public int getId_rol() {
        return id_rol;
    }
    
    public static void cerrarSesion() {
        instancia = null;
    }   
    
}