package models;

public class Student {
    private int id;
    private String rut;
    private String nombre;
    private Course curso;

    public Student() {
    }

    public Student(int id, String rut, String nombre, Course curso) {
        this.rut = rut;
        this.nombre = nombre;
        this.curso = curso;
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

    @Override
    public String toString() {
        return "Student{" + "id:" + id + ", rut:" + rut + ", nombre:" + nombre + ", curso:" + curso + '}';
    }
    
}
