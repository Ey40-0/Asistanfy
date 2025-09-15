package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Alumno;

public class StudentsController {
    
    @FXML
    private TableView<Alumno> tableTests;
    @FXML
    private TableColumn<Alumno, String> colTitle;
    @FXML
    private TableColumn<Alumno, String> colDate;
    
    @FXML
    public void initialize() {
        
    }
    
}
