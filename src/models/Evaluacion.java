package models;

import java.time.LocalDate;

public class Evaluacion {
    private int id;
    private String titulo;
    private LocalDate fecha;
    private Curso curso;
    private Asignatura asignatura;
    private int idProfesor;
    private int is_active;

    public Evaluacion() {
    }

    public Evaluacion(int id, String titulo, LocalDate fecha, Curso curso, Asignatura asignatura, int idProfesor, int is_active) {
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
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
