package models;

import java.time.LocalDate;

public class Evaluacion {
    private int id;
    private String titulo;
    private LocalDate fecha;
    private Curso curso;
    private Asignatura asignatura;
    private int idProfesor;

    public Evaluacion() {
    }

    public Evaluacion(int id, String titulo, LocalDate fecha, Curso curso, Asignatura asignatura, int idProfesor) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.curso = curso;
        this.asignatura = asignatura;
        this.idProfesor = idProfesor;
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
    
    
}
