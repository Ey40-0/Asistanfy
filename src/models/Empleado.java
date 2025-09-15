package models;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String contrasenia;
    private String email;
    private int tipo;
    private int activa;
    private String codigo;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellido, String contrasenia, String email, int tipo, int activa, String codigo) {
        this.id = id;
        this.nombre= nombre;
        this.apellido = apellido;
        this.contrasenia = contrasenia;
        this.email = email;
        this.tipo = tipo;
        this.activa = activa;
        this.codigo = codigo;
        
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
