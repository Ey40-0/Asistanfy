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
import models.Empleado;
import models.Evaluacion;
import models.EvaluacionDAO;
import models.Sesion;

public class TestsController {

    @FXML
    private TableView<Evaluacion> tableTests;
    @FXML
    private TableColumn<Evaluacion, String> colTitle;
    @FXML
    private TableColumn<Evaluacion, String> colDate;
    @FXML
    private TableColumn<Evaluacion, String> colCourse;
    @FXML
    private TableColumn<Evaluacion, String> colMatter;
    @FXML
    private TableColumn<Evaluacion, Button> colDetails;

    private final EvaluacionDAO evac = new EvaluacionDAO();

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
            int rol = Sesion.getInstance().getId_rol();

            if (rol == 0) { // Profesor
                btn.setText("Añadir");
                btn.setOnAction(e -> {
                    // Aquí podrías pasar la evaluación seleccionada
                    Evaluacion eval = getTableView().getItems().get(getIndex());
                    MenuAutoController.getInstance().loadPanel("/views/mp_alumnos.fxml");
                });
            } else { // Inspector
                btn.setText("Detalles");
                btn.setOnAction(e -> {
                    Evaluacion eval = getTableView().getItems().get(getIndex());
                    MenuAutoController.getInstance().loadPanel("/views/mp_view_alumnos.fxml");
                });
            }

            setGraphic(btn);
        }
    });

        loadTests();
    }

    public void loadTests() {
        ObservableList<Evaluacion> tests;
        
        if (Sesion.getInstance().getId_rol() == 0) {
            tests = FXCollections.observableArrayList(
                evac.getEvaluacionesByProfesor(Sesion.getInstance().getId())
            );
        } else {
            
            // Consigue los datos del empleado seleccionado (Necesariamente profesor)
            int id = MostrarProfesores.getInstance().getInfoEmpleado();
            tests = FXCollections.observableArrayList(
                evac.getEvaluacionesByProfesor(id)
            );
        }    
        tableTests.setItems(tests);
    }
    
}