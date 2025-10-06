package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Course;
import models.CourseC;
import models.MatterC;
import models.Session;
import models.Student;
import models.StudentC;

public class GralReportCllr {

    @FXML private ListView<Student> listStudents;
    @FXML private TextField fldSearch;
    @FXML private TableView<CourseReport> tableDataCourses;
    @FXML private TableColumn<CourseReport, String> colName;
    @FXML private TableColumn<CourseReport, Integer> colLeft;
    @FXML private TableColumn<CourseReport, String> colMostLeft;
    @FXML private TableColumn<CourseReport, String> colMatterLt;

    // Instancias compartidas de los modelos
    private final CourseC courseModel = new CourseC();
    private final StudentC studentModel = new StudentC();
    private final MatterC matterModel = new MatterC();
    
    private FilteredList<Student> filteredStuds;  
    private Student selected;

    @FXML
    public void initialize() {
        
        // Mostrar datos de la tabla
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombreCurso()));
        colLeft.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getTotalLefts()).asObject());
        colMostLeft.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentMostLeft()));
        colMatterLt.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMatterDislike()));
        
        // Listener del filtro
        listStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selected = newSel;
            if (selected != null) {
                showStudentData();
            }
        });

        // Listener para búsqueda
        fldSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtro = (newValue == null) ? "" : newValue.toLowerCase();

            filteredStuds.setPredicate(stu -> {
                if (filtro.isEmpty()) return true;

                String name = stu.getNombre().toLowerCase();
                String run = stu.getRut();

                return name.contains(filtro) || run.contains(filtro);
            });
        });

        loadData();
    }

    private void loadData() {     
        // TableView
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
        
        // ListView
        ObservableList<Student> students = studentModel.getAllStudents();

        filteredStuds = new FilteredList<>(students, e -> true);

        listStudents.setItems(filteredStuds);

        listStudents.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student stu, boolean empty) {
                super.updateItem(stu, empty);
                if (empty || stu == null) {
                    setText(null);
                } else {
                    setText(stu.getNombre() + " " + stu.getRut());
                }
            }
        });
        
    }
    
    public void showStudentData() {
        if (selected != null) {
            Session.getInstance().setSelectedStud(selected);
            GuideCllr.getInstance().loadPanel("/views/StudentReportVw.fxml");
        } else {
            MainCllr.mostrarAlerta("Error", "Seleccione un estudiente por favor.");
        }
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
