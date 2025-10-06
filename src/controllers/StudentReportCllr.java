package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import models.Session;
import models.Student;
import models.StudentC;

public class StudentReportCllr {

    @FXML private Label lblData;
    @FXML private Label lblLefts;
    @FXML private Label lblJustificated;
    @FXML private Label lblNoJustificated;
    @FXML private Label lblMatter;
    @FXML private Label lblLast;
    
    @FXML private TableView<Student> tableStudent;
    Student stu = Session.getInstance().getSelectedStud();
    
    @FXML
    public void initialize() {
        lblData.setText(stu.getNombre()+" - "+stu.getRut());
        lblLefts.setText(StudentC.countLefts(stu.getId()));
        lblJustificated.setText(string);
        lblNoJustificated.setText(string);
        lblMatter.setText(string);
        lblLast.setText(string);
    }

}
