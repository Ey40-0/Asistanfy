package models;

public class Matter {
    private int id;
    private String nombre;

    /**
     * Constructor de Matter.
     * @param id ID de la materia.
     * @param nombre Nombre de la materia.
     */
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

    /**
     * Representación string de la materia (solo nombre).
     * @return El nombre de la materia.
     */
    @Override
    public String toString() {
        return nombre;
    }
 
}