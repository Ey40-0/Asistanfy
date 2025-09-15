package models;

public class Sesion {
    private static Sesion instancia;
    private int id;
    private int id_rol;
    private String code;

    public Sesion() {
        
    }

    public Sesion(int id, int id_rol, String code) {
        this.id = id;
        this.id_rol = id_rol;
        this.code = code;
    }
    
    public static void iniciarSesion(int id, int id_rol, String code) {
        // Siempre crea una nueva sesión, reemplazando la anterior
        instancia = new Sesion(id, id_rol, code);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}