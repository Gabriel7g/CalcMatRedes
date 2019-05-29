
package franco.daniel.calculadora;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    
    private final StringProperty nome;
    private final StringProperty tipo;
    private final StringProperty quantidade;
    
    public Item(String nome, String tipo, String quantidade) {
        this.nome = new SimpleStringProperty(nome);
        this.tipo = new SimpleStringProperty(tipo);
        this.quantidade = new SimpleStringProperty(quantidade);
    }
    
    public Item(String nome, String tipo, int quantidade) {
        this(nome, tipo, Integer.toString(quantidade));
    }

    public String getNome() {
        return nome.get();
    }
    
    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getTipo() {
        return tipo.get();
    }
    
    public StringProperty tipoProperty() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }
    
    public String getQuantidade() {
        return quantidade.get();
    }
    
    public StringProperty quantidadeProperty() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade.set(quantidade);
    }
    
}
