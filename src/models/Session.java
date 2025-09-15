package models;

public class Session {
    private static Session instancia;
    private int id;
    private int id_rol;

    public Session() {
        
    }

    public Session(int id, int id_rol) {
        this.id = id;
        this.id_rol = id_rol;
    }
    
    public static void iniciarSesion(int id, int id_rol) {
        // Siempre crea una nueva sesión, reemplazando la anterior
        instancia = new Session(id, id_rol);
    }

    public static Session getInstance() {
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