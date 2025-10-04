package models;

public class Employee {
    private int id;
    private String nombre;
    private String apellido;
    private String contrasenia;
    private String email;
    private int tipo;
    private int activa;

    /**
     * Constructor vacío.
     */
    public Employee() {
    }

    /**
     * Constructor completo.
     * @param id ID del empleado.
     * @param nombre Nombre.
     * @param apellido Apellido.
     * @param email Email.
     * @param contrasenia Contraseña.
     * @param tipo Tipo (rol).
     * @param activa Estado activo.
     */
    public Employee(int id, String nombre, String apellido, String email, String contrasenia, int tipo, int activa) {
        this.id = id;
        this.nombre= nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
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

    /**
     * Representación string del empleado.
     * @return String con todos los atributos.
     */
    @Override
    public String toString() {
        return "Employee{" + "id:" + id + ", nombre:" + nombre + ", apellido:" + apellido + ", contrasenia:" + contrasenia + ", email:" + email + ", tipo:" + tipo + ", activa:" + activa + '}';
    }
    
}