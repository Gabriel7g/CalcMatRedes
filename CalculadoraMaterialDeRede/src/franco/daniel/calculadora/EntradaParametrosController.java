/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package franco.daniel.calculadora;

import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class EntradaParametrosController implements Controller {

    @FXML private TextField pontosTelecomField;
    @FXML private TextField pontosSimplesField;
    @FXML private TextField pontosCFTVField;
    @FXML private TextField pontosVozField;
    @FXML private RadioButton rackFechadoRadio;
    @FXML private RadioButton rackAbertoRadio;
    @FXML private TextField limiteMHField;
    @FXML private ComboBox<String> catComboBox;
    
    public static final String[] CATEGORIAS = {"5", "5e", "6", "6a", "7", "7a"};
    private Stage stage;
    
    @Override
    public void initialize() {
        TextField[] fields = {
            pontosTelecomField, pontosSimplesField, pontosCFTVField, pontosVozField
        };
        
        for (TextField field : fields) {
            field.setTextFormatter(new TextFormatter<>(c -> {
                String text = c.getText();
                if (text.matches("\\d*") && field.getLength() < 6)
                    return c;
                return null;
            }));
        }
        
        ToggleGroup rackSalaFechada = new ToggleGroup();
        
        rackFechadoRadio.setToggleGroup(rackSalaFechada);
        rackAbertoRadio.setToggleGroup(rackSalaFechada);
        
        catComboBox.getItems().addAll(CATEGORIAS);
        catComboBox.getSelectionModel().select("6");
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private void calcularHandler() {
        
        Map<String, Integer> params = new HashMap<>();
        
        try {
            params.put("pontosTelecom", Integer.parseInt(pontosTelecomField.getText()));
            params.put("pontosSimples", Integer.parseInt(pontosSimplesField.getText()));
            params.put("pontosCFTV", Integer.parseInt(pontosCFTVField.getText()));
            params.put("pontosVoz", Integer.parseInt(pontosVozField.getText()));
            params.put("rackFechado", rackFechadoRadio.isSelected() ? 1 : 0);
            params.put("limiteMalhaHorizontal", Integer.parseInt(limiteMHField.getText()));
            params.put("categoria", catComboBox.getSelectionModel().getSelectedIndex());
            Modal.builder(TabelaMaterialController.class)
                    .ownerStage(stage)
                    .title("Tabela ─ Quantidade de Materiais de Rede")
                    .controllerAction(ctrl -> ctrl.setParametros(params))
                    .build()
                    .show();
        } catch (NumberFormatException ex) {
            new Alert.AlertBuilder("Preencha todos os campos corretamente")
                    .type(AlertType.WARNING)
                    .build()
                    .show();
        }
    }
    
}
