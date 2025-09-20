package models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Student {
    private int id;
    private String rut;
    private String nombre;
    private Course curso;
    private final BooleanProperty justification = new SimpleBooleanProperty(false);

    public Student() {
    }

    public Student(int id, String rut, String nombre, Course curso) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.curso = curso;
        this.justification.set(false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Course getCurso() {
        return curso;
    }

    public void setCurso(Course curso) {
        this.curso = curso;
    }

    // JUSTIFICATION (BooleanProperty)
    public boolean isJustification() {
        return justification.get();
    }

    public void setJustification(boolean value) {
        this.justification.set(value);
    }

    public BooleanProperty justificationProperty() {
        return justification;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id:" + id +
                ", rut:" + rut +
                ", nombre:" + nombre +
                ", curso:" + curso +
                ", justification:" + isJustification() +
                '}';
    }
}