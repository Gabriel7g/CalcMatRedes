
package franco.daniel.calculadora;

/**
 * Indica um erro de construção ocorrido em uma classe Builder ou Factory.
 */
public class InvalidBuildException extends Exception {

    private static final String MESSAGE = "Erro no build do objeto";
    
    public InvalidBuildException(String message) {
        super(message);
    }
    
    public InvalidBuildException() {
        super(MESSAGE);
    }
    
    public InvalidBuildException(Throwable cause) {
        super(MESSAGE, cause);
    }
    
}
