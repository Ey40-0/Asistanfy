package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Employee;
import models.Test;
import models.TestC;
import models.Session;

public class ShowTestsCllr {

    @FXML
    private TableView<Test> tableTests;
    @FXML
    private TableColumn<Test, String> colTitle;
    @FXML
    private TableColumn<Test, String> colDate;
    @FXML
    private TableColumn<Test, String> colCourse;
    @FXML
    private TableColumn<Test, String> colMatter;
    @FXML
    private TableColumn<Test, Button> colDetails;

    private final TestC evac = new TestC();

    @FXML
    public void initialize() {
        
        // configuracion de columnas
        colTitle.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));
        colCourse.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCurso().getNombre()));
        colMatter.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAsignatura().getNombre()));

        // botón addAlumnos-detalles
        colDetails.setCellFactory(tc -> new TableCell<>() {
        private final Button btn = new Button();

        {
            setAlignment(Pos.CENTER);
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        @Override
        protected void updateItem(Button item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
                return;
            }

            // Verifica el rol
            int rol = Session.getInstance().getId_rol();

            if (rol == 0) { // Profesor
                btn.setText("Añadir");
                btn.setOnAction(e -> {
                    // Aquí podrías pasar la evaluación seleccionada
                    Test eval = getTableView().getItems().get(getIndex());
                    GuideCllr.getInstance().loadPanel("/views/MpAddStudVw.fxml");
                });
            } else { // Inspector
                btn.setText("Detalles");
                btn.setOnAction(e -> {
                    Test eval = getTableView().getItems().get(getIndex());
                    GuideCllr.getInstance().loadPanel("/views/ShowStudVw.fxml");
                });
            }

            setGraphic(btn);
        }
    });

        loadTests();
    }

    public void loadTests() {
        ObservableList<Test> tests;
        
        if (Session.getInstance().getId_rol() == 0) {
            tests = FXCollections.observableArrayList(evac.getEvaluacionesByProfesor(Session.getInstance().getId())
            );
        } else {
            
            // Consigue los datos del empleado seleccionado (Necesariamente profesor)
            int id = ShowTeachersCllr.getInstance().getInfoEmpleado();
            tests = FXCollections.observableArrayList(
                evac.getEvaluacionesByProfesor(id)
            );
        }    
        tableTests.setItems(tests);
    }
    
}