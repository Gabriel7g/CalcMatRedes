
package franco.daniel.calculadora;

import javafx.scene.control.Alert.AlertType;

/**
 * Representação de uma caixa de alerta da aplicação.
 * Objetos dessa classe são imutáveis, mas a sua criação é controlada por
 * meio da adoção do padrão de projeto Builder com Flow Interfaces.
 */
public class Alert {
    
    private final AlertType type;
    private final String title;
    private final String header;
    private final String content;
    private final javafx.scene.control.Alert alert;

    public Alert(AlertBuilder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.header = builder.header;
        this.content = builder.content;
        this.alert = builder.alert;
    }

    public AlertType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public void show() {
        alert.show();
    }    
    
    public void showAndWait() {
        alert.showAndWait();
    }
    
    public static class AlertBuilder {
        
        private final String content;
        private AlertType type;
        private String title;
        private String header;
        
        private javafx.scene.control.Alert alert;

        public AlertBuilder(String content) {
            this.content = content;
        }
        
        public AlertBuilder type(AlertType type) {
            this.type = type;
            return this;
        }
        
        public AlertBuilder title(String title) {
            this.title = title;
            return this;
        }
        
        public AlertBuilder header(String header) {
            this.header = header;
            return this;
        }
        
        public Alert build() {
            try {
                validateObject();

                alert = new javafx.scene.control.Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                
                return new Alert(this);
            } catch (InvalidBuildException ex) {
                System.out.println(ex);
                return null;
            }
        }
        
        private void validateObject() throws InvalidBuildException {
            if (type == null) {
                type = AlertType.INFORMATION;
            }
        }
        
    }
    
}
