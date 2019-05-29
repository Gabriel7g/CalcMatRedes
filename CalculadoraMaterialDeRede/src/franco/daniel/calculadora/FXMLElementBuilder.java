
package franco.daniel.calculadora;

import java.io.IOException;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;

/**
 * Classe construtora de elementos JavaFX a partir de arquivos FXML e de
 * controllers.
 * 
 * @param <T> Controller associado ao FXML
 */
public class FXMLElementBuilder<T extends Controller> {
    
    private final Class<T> controllerClass;
    private String locationPath;
    private Consumer<T> controllerAction;

    public FXMLElementBuilder(Class<T> controllerClass) {
        if (controllerClass == null) {
            throw new IllegalArgumentException(
                    "Não devem haver argumentos nulos na construção.");
        }
        this.controllerClass = controllerClass;
    }
    
    public FXMLElementBuilder<T> locationPath(String locationPath) {
        this.locationPath = locationPath;
        return this;
    }
    
    public FXMLElementBuilder<T> controllerAction(Consumer<T> controllerAction) {
        this.controllerAction = controllerAction;
        return this;
    }
    
    public <U> U build() {
        try {
            validateObject();
            
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(controllerClass.getResource(locationPath));
            
            U element = loader.load();
            T controller = loader.getController();
            if (controllerAction != null) {
                controllerAction.accept(controller);
            }
            
            return element;
        } catch (IOException | InvalidBuildException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            return null;
        }
    }
    
    private void validateObject() throws InvalidBuildException {
        if (locationPath == null) {
                locationPath = String.format("/franco/daniel/calculadora/%s.fxml",
                        controllerClass.getSimpleName().replace("Controller", ""));
            }
    }
    
}
