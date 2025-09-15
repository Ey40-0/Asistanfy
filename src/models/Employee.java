package models;

public class Employee {
    private int id;
    private String nombre;
    private String apellido;
    private String contrasenia;
    private String email;
    private int tipo;
    private int activa;

    public Employee() {
    }

    public Employee(int id, String nombre, String apellido, String contrasenia, String email, int tipo, int activa) {
        this.id = id;
        this.nombre= nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.email = email;
        this.tipo = tipo;
        this.activa = activa;       
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }   
    
}
