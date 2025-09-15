package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Student;

public class ShowStudentsCllr {
    
    @FXML
    private TableView<Student> tableTests;
    @FXML
    private TableColumn<Student, String> colTitle;
    @FXML
    private TableColumn<Student, String> colDate;
    
}
