
package franco.daniel.calculadora;

import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Representação de uma janela modal da aplicação.
 * Objetos dessa classe são imutáveis, mas a sua criação é controlada por
 * meio da adoção do padrão de projeto Builder com Flow Interfaces.
 * 
 * @param <T> Controller associado à modal
 */
public class Modal<T extends ModalController> {
    
    private final String viewLocationPath;
    private final String title;
    private final Stage ownerStage;
    private final Stage modalStage;
    private final T controller;
    
    private Modal(ModalBuilder<T> builder) {
        this.viewLocationPath = builder.viewLocationPath;
        this.title = builder.title;
        this.ownerStage = builder.ownerStage;
        this.modalStage = builder.modalStage;
        this.controller = builder.controller;
    }

    public String getViewLocationPath() {
        return viewLocationPath;
    }

    public String getTitle() {
        return title;
    }

    public Stage getOwnerStage() {
        return ownerStage;
    }
    
    public T getController() {
        return controller;
    }
    
    public void show() {
        modalStage.show();
    }
    
    public <U> U showAndWait(Function<T,U> returnAction) {
        modalStage.showAndWait();
        return returnAction == null ? null : returnAction.apply(controller);
    }
    
    public void showAndWait() {
        showAndWait(null);
    }

    public static <U extends ModalController> ModalBuilder<U> builder(
            Class<U> controllerClass) {
        return new ModalBuilder<>(controllerClass);
    }
    
    /**
     * Classe construtora de Modal. Utliziza uma Flow Interface para a definição
     * de atributos opcionais.
     * 
     * @param <C> Controller associado à modal
     */
    public static class ModalBuilder<C extends ModalController> {
                
        private String viewLocationPath;
        private String title;
        private Stage ownerStage;
        
        private final Class<C> controllerClass;
        private Consumer<C> controllerAction;
        private Stage modalStage;
        private C controller;
        
        public ModalBuilder(Class<C> controllerClass) {
            if (controllerClass == null) {
                throw new IllegalArgumentException(
                        "Não devem haver argumentos nulos na construção.");
            }
            this.controllerClass = controllerClass;
        }
        
        public ModalBuilder<C> title(String title) {
            this.title = title;
            return this;
        }
        
        public ModalBuilder<C> ownerStage(Stage ownerStage) {
            this.ownerStage = ownerStage;
            return this;
        }
        
        public ModalBuilder<C> viewLocationPath(String viewLocationPath) {
            this.viewLocationPath = viewLocationPath;
            return this;
        }
        
        public ModalBuilder<C> controllerAction(Consumer<C> controllerAction) {
            this.controllerAction = controllerAction;
            return this;
        } 
        
        public Modal<C> build() {            
            try {
                validateObject();
                
                modalStage = new Stage();
                modalStage.initModality(Modality.WINDOW_MODAL);
                modalStage.setTitle(title);
                modalStage.initOwner(ownerStage);
                
                Scene scene = new Scene(new FXMLElementBuilder<>(controllerClass)
                        .locationPath(viewLocationPath)
                        .controllerAction(controllerAction
                                .andThen(ctrl -> ctrl.setModalStage(modalStage))
                                .andThen(ctrl -> controller = ctrl))
                        .build());

                modalStage.setScene(scene);
                
                return new Modal<>(this);
            } catch (InvalidBuildException ex) {
                System.out.println(ex);
                return null;
            } 
        }
        
        private void validateObject() throws InvalidBuildException {
            if (viewLocationPath == null) {
                viewLocationPath = String.format("/franco/daniel/calculadora/%s.fxml",
                        controllerClass.getSimpleName().replace("Controller", ""));
            }
        }
        
    }
            
}
