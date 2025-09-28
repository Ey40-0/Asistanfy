package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import models.Session;
import models.Student;
import models.StudentC;

public class ShowStudentsCllr {
    
    @FXML private TableView<Student> tableStuds;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colRun;
    @FXML private TableColumn<Student, Boolean> colJustification;
    @FXML private TableColumn<Student, Button> colUpdate;
    
    private final StudentC stuc = new StudentC();
    
    @FXML
    public void initialize() {
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colRun.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRut()));

        if (Session.getInstance().getId_rol() == 0) {
            // Profesor: solo visual
            colJustification.setCellValueFactory(cellData -> {
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().bind(cellData.getValue().justificationProperty());
                checkBox.setDisable(true);
                return new SimpleObjectProperty<>(checkBox.isSelected());
            });
        } else {
            // Inspector: editable
            colJustification.setCellValueFactory(cellData -> cellData.getValue().justificationProperty());
            colJustification.setCellFactory(CheckBoxTableCell.forTableColumn(colJustification));
            tableStuds.setEditable(true);
        }
        
        colUpdate.setCellFactory(tc -> new TableCell<>() {
            private final Button upd = new Button("Modificar");
            {
                upd.setOnAction(e -> {           
                    Student stu = getTableView().getItems().get(getIndex());
                    Session.getInstance().setSelectedStud(stu);
                    GuideCllr.getInstance().loadPanel("/views/MpAddStudVw.fxml");
                });
            }
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(upd);
                }
            }
        });
        loadStuds();
    }
    
    public void loadStuds() {
        ObservableList<Student> studs = FXCollections.observableArrayList(
            stuc.getStudentsByTest(Session.getInstance().getSelectedTest().getId())
        );

        // Agregar listener para actualizar en BD
        for (Student stud : studs) {
            stud.justificationProperty().addListener((obs, oldVal, newVal) -> {
                stuc.updateJustification(stud.getId(), newVal);
            });
        }

        tableStuds.setItems(studs);
    }
    
    public void btnAddStud() {
        if (Session.getInstance().getId_rol() == 1) {
            MainCllr.mostrarAlerta("Error", "Solo profesores pueden añadir ausentes.");
            return;
        }
        GuideCllr.getInstance().loadPanel("/views/MpAddStudVw.fxml");
    }
    
    public void btnViewTest() {
        GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
    }

}
