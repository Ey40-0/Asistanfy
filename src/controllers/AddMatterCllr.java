package controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Matter;

public class AddMatterCllr {
    
    @FXML private TextField fldName;
    @FXML private ListView<Matter> listMatters;
    @FXML private TextField fldSearch;
    
    private FilteredList<Matter> filteredMatters;
    private Matter selected;
    
    @FXML
    public void initialize() {
        loadMatters();
        // Listener de selección
        listMatters.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selected = newSel;
            if (selected != null) {
                fldName.setText(selected.getNombre());
            } else {
                fldName.clear(); // Por si se deselecciona
            }
        });
    }
    
    public void register() {
        String name = fldName.getText().trim();
        
        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos.");
            return;
        }
        
        Matter mat = new Matter(0, name);
        if (Matter.addNewMatter(mat)) {
            fldName.setText("");
            loadMatters();
        }
    }

    private void loadMatters() {
        ObservableList<Matter> materias = Matter.getMatters();

        // Creamos la lista filtrada
        filteredMatters = new FilteredList<>(materias, m -> true);

        // Asignamos al ListView
        listMatters.setItems(filteredMatters);

        // Configuramos la visualización
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

        // Listener para filtrar en tiempo real
        fldSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtro = (newValue == null) ? "" : newValue.toLowerCase();

            filteredMatters.setPredicate(mat -> {
                if (filtro.isEmpty()) return true;

                // Filtrar por nombre de la materia
                return mat.getNombre().toLowerCase().contains(filtro);
            });
        });
    }
    
    public void deleteMatter() {
        if (selected != null) {
            Matter.deleteMatter(selected.getId());
            loadMatters();
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona una materia para borrar.");
        }
    }
    
    @FXML
    private void updateMatter() {
        if (selected != null) {
            String name = fldName.getText().trim();
            
            if (name.isEmpty()) {
                MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos.");
                return;
            }
            
            Matter mat = new Matter (selected.getId(), name);
            if (Matter.updateMatter(mat)) {
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
