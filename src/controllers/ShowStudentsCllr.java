package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Alumno;

public class ShowStudentsCllr {
    
    @FXML
    private TableView<Alumno> tableTests;
    @FXML
    private TableColumn<Alumno, String> colTitle;
    @FXML
    private TableColumn<Alumno, String> colDate;
    
}
