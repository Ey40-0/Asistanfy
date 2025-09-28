package controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Course;
import models.CourseC;

public class AddCourseCllr {

    @FXML private TextField fldName;
    @FXML private TextField fldSearch;
    @FXML private ListView<Course> listCourses;

    private Course selected;
    private FilteredList<Course> filteredCourses;
    private final CourseC couc = new CourseC();

    @FXML
    public void initialize() {
        loadCourses();

        listCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selected = newSel;
            if (selected != null) {
                fldName.setText(selected.getNombre());
            } else {
                fldName.clear();
            }
        });

        fldSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal == null ? "" : newVal.toLowerCase();
            filteredCourses.setPredicate(course -> course.getNombre().toLowerCase().contains(filter));
        });
    }

    @FXML
    private void registerCourse() {
        String name = fldName.getText().trim();

        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacíos", "Por favor, complete el nombre del curso.");
            return;
        }

        Course course = new Course(0, name);
        if (couc.addNewCourse(course)) {
            clearFields();
            loadCourses();
        } else {
            MainCllr.mostrarAlerta("Error", "No se pudo registrar el curso.");
        }
    }

    @FXML
    private void updateCourse() {
        if (selected != null) {
            String name = fldName.getText().trim();

            if (name.isEmpty()) {
                MainCllr.mostrarAlerta("Campos vacíos", "Por favor, complete el nombre del curso.");
                return;
            }

            Course cou = new Course(selected.getId(), name);
            if (couc.updateCourse(cou)) {
                clearFields();
                loadCourses();
            } else {
                MainCllr.mostrarAlerta("Error", "No se pudo actualizar el curso.");
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor seleccione un curso para modificar.");
        }
    }

    @FXML
    private void deleteCourse() {
        if (selected != null) {
            couc.deleteCourse(selected.getId());
            clearFields();
            loadCourses();
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor seleccione un curso para eliminar.");
        }
    }

    private void loadCourses() {
        ObservableList<Course> courses = CourseC.getCourses();
        filteredCourses = new FilteredList<>(courses, c -> true);
        listCourses.setItems(filteredCourses);

        listCourses.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    private void clearFields() {
        fldName.clear();
        fldSearch.clear();
        listCourses.getSelectionModel().clearSelection();
        selected = null;
    }
}
