package models;

import java.time.LocalDate;

public class Test {
    private int id;
    private String titulo;
    private LocalDate fecha;
    private Course curso;
    private Matter asignatura;
    private int idProfesor;
    private int is_active;

    /**
     * Constructor vacío.
     */
    public Test() {
    }

    /**
     * Constructor completo.
     * @param id ID de la prueba.
     * @param titulo Título.
     * @param fecha Fecha.
     * @param curso Curso asociado.
     * @param asignatura Asignatura.
     * @param idProfesor ID del profesor.
     * @param is_active Estado activo.
     */
    public Test(int id, String titulo, LocalDate fecha, Course curso, Matter asignatura, int idProfesor, int is_active) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.curso = curso;
        this.asignatura = asignatura;
        this.idProfesor = idProfesor;
        this.is_active = is_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Course getCurso() {
        return curso;
    }

    public void setCurso(Course curso) {
        this.curso = curso;
    }

    public Matter getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Matter asignatura) {
        this.asignatura = asignatura;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
    
    
}