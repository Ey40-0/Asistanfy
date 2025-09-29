package models;

public class Course {
    private int id;
    private String nombre;

    /**
     * Constructor de Course.
     * @param id ID del curso.
     * @param nombre Nombre del curso.
     */
    public Course(int id, String nombre) {
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

    /**
     * Representación string del curso (solo nombre).
     * @return El nombre del curso.
     */
    @Override
    public String toString() {
        return nombre;
    }
    
}