
package franco.daniel.calculadora;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** 
 * Calculadora CalcMatRedes.
 * De Daniel Franco (a.k.a Daniekans) e Gabriel Cruz (a.k.a Itabirito)
 * 2019
 */

public class CalculadoraApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
                
        AnchorPane inputPane = new FXMLElementBuilder<>(EntradaParametrosController.class)
                .controllerAction(ctrl -> ctrl.setStage(primaryStage))
                .build();
        
        StackPane root = new StackPane(inputPane);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Calculadora de Material de Rede");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
