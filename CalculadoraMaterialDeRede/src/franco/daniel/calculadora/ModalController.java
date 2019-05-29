
package franco.daniel.calculadora;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Representa o controler de um elemento modal da aplicação JavaFX.
 */
public abstract class ModalController implements Controller {
    
    @FXML private Stage modalStage;
    
    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }
    
    public Stage getModalStage() {
        return modalStage;
    }
    
}
