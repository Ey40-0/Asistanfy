package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Course;
import models.CourseC;
import models.MatterC;
import models.StudentC;

public class GralReportCllr {

    @FXML private TableView<CourseReport> tableDataCourses;
    @FXML private TableColumn<CourseReport, String> colName;
    @FXML private TableColumn<CourseReport, Integer> colLeft;
    @FXML private TableColumn<CourseReport, String> colMostLeft;
    @FXML private TableColumn<CourseReport, String> colMatterLt;

    // Instancias compartidas de los modelos
    private final CourseC courseModel = new CourseC();
    private final StudentC studentModel = new StudentC();
    private final MatterC matterModel = new MatterC();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombreCurso()));
        colLeft.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTotalLefts()).asObject());
        colMostLeft.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentMostLeft()));
        colMatterLt.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMatterDislike()));

        loadData();
    }

    private void loadData() {
        ObservableList<Course> courses = FXCollections.observableArrayList(courseModel.getCourses());

        // Convierte los cursos a CourseReport, calculando los datos solo una vez
        ObservableList<CourseReport> reportList = FXCollections.observableArrayList();
        for (Course c : courses) {
            // Datos a mostrar
            int totalLefts = courseModel.totalLefts(c.getId());
            String studentMostLeft = studentModel.studentMostLeft(c.getId());
            String matterDislike = matterModel.matterDislike(c.getId());

            reportList.add(new CourseReport(c.getNombre(), totalLefts, studentMostLeft, matterDislike));
        }

        tableDataCourses.setItems(reportList);
    }

    // Clase para los datos del reporte
    public static class CourseReport {
        
        private final String nombreCurso;
        private final int totalLefts;
        private final String studentMostLeft;
        private final String matterDislike;

        public CourseReport(String nombreCurso, int totalLefts, String studentMostLeft, String matterDislike) {
            this.nombreCurso = nombreCurso;
            this.totalLefts = totalLefts;
            this.studentMostLeft = studentMostLeft;
            this.matterDislike = matterDislike;
        }

        public String getNombreCurso() { return nombreCurso; }
        public int getTotalLefts() { return totalLefts; }
        public String getStudentMostLeft() { return studentMostLeft; }
        public String getMatterDislike() { return matterDislike; }
        
    }
    
}
