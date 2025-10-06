package controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Matter;
import models.MatterC;

public class AddMatterCllr {
    
    @FXML private TextField fldName;
    @FXML private ListView<Matter> listMatters;
    @FXML private TextField fldSearch;
    
    private FilteredList<Matter> filteredMatters;
    private Matter selected;
    
    /**
     * Inicializa el controlador, cargando las materias y configurando listeners.
     */
    @FXML
    public void initialize() {
        loadMatters();
        listMatters.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selected = newSel;
            if (selected != null) {
                fldName.setText(selected.getNombre());
            } else {
                fldName.clear();
            }
        });
    }
    
    /**
     * Registra una nueva materia en la base de datos.
     */
    public void register() {
        String name = fldName.getText().trim();
        
        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos.");
            return;
        }
        
        Matter mat = new Matter(0, name);
        if (MatterC.addNewMatter(mat)) {
            fldName.setText("");
            loadMatters();
        }
    }

    /**
     * Carga la lista de materias desde la base de datos.
     */
    private void loadMatters() {
        ObservableList<Matter> materias = MatterC.getMatters();

        filteredMatters = new FilteredList<>(materias, m -> true);

        listMatters.setItems(filteredMatters);

        listMatters.setCellFactory(lv -> new ListCell<Matter>() {
            @Override
            protected void updateItem(Matter mat, boolean empty) {
                super.updateItem(mat, empty);
                if (empty || mat == null) {
                    setText(null);
                } else {
                    setText(mat.getNombre());
                }
            }
        });

        fldSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtro = (newValue == null) ? "" : newValue.toLowerCase();

            filteredMatters.setPredicate(mat -> {
                if (filtro.isEmpty()) return true;

                return mat.getNombre().toLowerCase().contains(filtro);
            });
        });
    }
    
    /**
     * Elimina la materia seleccionada (setea is_active=0).
     */
    public void deleteMatter() {
        if (selected != null) {
            MatterC.deleteMatter(selected.getId());
            loadMatters();
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona una materia para borrar.");
        }
    }
    
    /**
     * Actualiza la materia seleccionada en la base de datos.
     */
    @FXML
    private void updateMatter() {
        if (selected != null) {
            String name = fldName.getText().trim();
            
            if (name.isEmpty()) {
                MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos.");
                return;
            }
            
            Matter mat = new Matter (selected.getId(), name);
            if (MatterC.updateMatter(mat)) {
                fldName.setText("");
            } else {
                MainCllr.mostrarAlerta("Error", "Ha ocurrido un error al actualizar.");
            }
            
            loadMatters();
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona una materia para modificar.");
        }
    }
  
}