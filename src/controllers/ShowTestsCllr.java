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
import javafx.scene.layout.HBox;
import models.Test;
import models.TestC;
import models.Session;

public class ShowTestsCllr {

    @FXML private TableView<Test> tableTests;
    @FXML private TableColumn<Test, String> colTitle;
    @FXML private TableColumn<Test, String> colDate;
    @FXML private TableColumn<Test, String> colCourse;
    @FXML private TableColumn<Test, String> colMatter;
    @FXML private TableColumn<Test, Button> colDetails;

    private final TestC evac = new TestC();

    /**
     * Inicializa el controlador, configurando columnas de la tabla.
     */
    @FXML
    public void initialize() {
        
        colTitle.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));
        colCourse.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCurso().getNombre()));
        colMatter.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAsignatura().getNombre()));

        colDetails.setCellFactory(tc -> new TableCell<>() {
            private final Button det = new Button("Ausentes");
            private final Button del = new Button("Borrar");
            private final HBox box = new HBox(10, det, del);

            {
                box.setAlignment(Pos.CENTER);

                det.setOnAction(e -> {           
                    Test eval = getTableView().getItems().get(getIndex());
                    Session.getInstance().setSelectedTest(eval);
                    GuideCllr.getInstance().loadPanel("/views/ShowStudVw.fxml");
                });

                del.setOnAction(e -> {
                    Test eval = getTableView().getItems().get(getIndex());
                    Session.getInstance().setSelectedTest(eval);
                    deleteTest();
                    getTableView().getItems().remove(eval);
                    loadTests(); // Recarga después de borrar
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        loadTests();
    }

    /**
     * Carga las pruebas desde la base de datos según el rol.
     */
    private void loadTests() {
        ObservableList<Test> tests;

        if (Session.getInstance().getEmployee().getTipo() == 0) {
            tests = FXCollections.observableArrayList(evac.getEvaluacionesByProfesor(Session.getInstance().getEmployee().getId()));
        } else {
            int id = Session.getInstance().getSelectedEmployeeId();
            tests = FXCollections.observableArrayList(
                evac.getEvaluacionesByProfesor(id)
            );
        }
        tableTests.setItems(tests);
    }
    
    /**
     * Elimina la prueba seleccionada.
     */
    private void deleteTest() {
        if (Session.getInstance().getSelectedTest() != null) {
            evac.deleteTest(Session.getInstance().getSelectedTest());
        }
    }
}