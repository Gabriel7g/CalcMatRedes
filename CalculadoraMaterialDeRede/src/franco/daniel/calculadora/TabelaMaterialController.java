
package franco.daniel.calculadora;

import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabelaMaterialController extends ModalController {
    
    @FXML private Accordion tabelasAccordion;
    @FXML private TableView<Item> principaisTable;
    @FXML private TableView<Item> miscelaneaTable;
    
    @FXML private TableColumn<Item, String> materialPrincipalCol;
    @FXML private TableColumn<Item, String> tipoPrincipalCol;
    @FXML private TableColumn<Item, String> quantidadePrincipalCol;
    
    @FXML private TableColumn<Item, String> materialMiscelaneaCol;
    @FXML private TableColumn<Item, String> tipoMiscelaneaCol;
    @FXML private TableColumn<Item, String> quantidadeMiscelaneaCol;
    
    @FXML private TextArea observacoesTextArea;
    
    private final String categoria = "6";
    private final int proporcaoPABX = 4;
    private final int numPortasPP = 24;
    private final int tamMinimoRack = 12;
    private final int tamMaximoRack = 48;
    private int pontosTelecom;
    private int pontosSimples;
    private int pontosCFTV;
    private int pontosVoz;
    private boolean rackFechado;
    private int limiteMalhaHorizontal;
    
    @Override
    public void initialize() {
        tabelasAccordion.setExpandedPane(tabelasAccordion.getPanes().get(0));
        
        PropertyValueFactory<Item, String> materialFac = new PropertyValueFactory<>("nome"),
                                           tipoFac = new PropertyValueFactory<>("tipo"),
                                           quantidadeFac = new PropertyValueFactory<>("quantidade");
        
        materialPrincipalCol.setCellValueFactory(materialFac);
        materialMiscelaneaCol.setCellValueFactory(materialFac);
        tipoPrincipalCol.setCellValueFactory(tipoFac);
        tipoMiscelaneaCol.setCellValueFactory(tipoFac);
        quantidadePrincipalCol.setCellValueFactory(quantidadeFac);
        quantidadeMiscelaneaCol.setCellValueFactory(quantidadeFac);
        
        observacoesTextArea.setText("Consideramos os seguintes valores:\n"
                + "- Rede estruturada conforme a categoria "+ categoria + ";\n"
                + "- Proporção do PABX de 1:" + proporcaoPABX + ";\n"
                + "- Limite de tamanho de malha horizontal de " + limiteMalhaHorizontal + " metros;\n"
                + "- Racks somente de piso (tamanhos de " + tamMinimoRack + "U a " + tamMaximoRack + "U);"
                + "- Quantidade de troncos vindos da operadora em conformidade com a quantidade de pontos de voz informada.");
    }
    
    public void setParametros(Map<String, Integer> params) {
        pontosTelecom = params.get("pontosTelecom");
        pontosSimples = params.get("pontosSimples");
        pontosCFTV = params.get("pontosCFTV");
        pontosVoz = params.get("pontosVoz");
        rackFechado = params.get("rackFechado") == 1;
        limiteMalhaHorizontal = params.get("limiteMalhaHorizontal");
        calcularItens();
    }
    
    private void calcularItens() {
        
        ObservableList<Item> listaPrincipais = FXCollections.observableArrayList(),
                             listaMisc = FXCollections.observableArrayList();

        /* Preenchimento da lista principal: */
        
        // ATR:
        
        int numTomadas = pontosTelecom * 2 + pontosSimples;
        
        listaPrincipais.add(new Item("Tomadas Fêmea", "RJ-45 Cat " + categoria, numTomadas));
        
        int numEspelhos = pontosTelecom + pontosCFTV + pontosVoz + pontosSimples;
        
        listaPrincipais.add(new Item("Espelhos", String.format("2x4\", furação %s",
                pontosSimples != 0 ? ("simples" + ((numEspelhos - pontosSimples == 0) ? "" : " e dupla")) : "dupla"), numEspelhos));
        
        int numPatchCords = (pontosTelecom + pontosCFTV) * 2 + pontosSimples;
        
        listaPrincipais.add(new Item("Patch Cords", "Flex, Cat " + categoria + ", azul, 3m", numPatchCords));
        
        int numEtiquetasPontos = (pontosTelecom + pontosCFTV + pontosVoz) * 3 + pontosSimples * 2;
        
        listaPrincipais.add(new Item("Etiquetas para Pontos de Telecom/Rede", "-", numEtiquetasPontos));
        
        // MH:
        
        int totalMalhaHorizontal = numTomadas * limiteMalhaHorizontal,
            caixasCabo = (int) Math.ceil((double) totalMalhaHorizontal / 305);
        
        listaPrincipais.add(new Item("Quantidade de Cabo para Malha Horizontal",
                "Cabo UTP (rígido), Cat " + categoria, String
                    .format("%d x %dm = %dm => %d caixas", numTomadas, limiteMalhaHorizontal,
                        totalMalhaHorizontal, caixasCabo)));
        
        int numEtiquetasMH = numTomadas * 2;
        
        listaPrincipais.add(new Item("Etiquetas", "Malha Horizontal", numEtiquetasMH));
        
        // SET:
        
        int numTroncos = (int) Math.ceil(pontosVoz * 2.0 / proporcaoPABX),        
            numPPDados = (int) Math.ceil((double) numPatchCords / numPortasPP),
            numPPTroncos = (int) Math.ceil((double) numTroncos / numPortasPP),
            numPPRamais = (int) Math.ceil(((double) numTroncos * proporcaoPABX) / numPortasPP),
            totalPP = numPPDados + 2 * numPPTroncos + numPPRamais;
        
        listaPrincipais.add(new Item("Patch Panels", numPortasPP + " portas, 1U, fixo, Cat " + categoria,
                String.format("%d (%d para dados, \n%d para troncos, %d para o PABX \ne %d para os ramais)",
                    totalPP, numPPDados, numPPTroncos, numPPTroncos, numPPRamais)));
        
        int numOrganizadoresFrontais = totalPP + numPPDados;
        System.out.println("numPPDados: " + numPPDados);
        System.out.println("Org: " + totalPP + " + " + numPPDados + " = " + numOrganizadoresFrontais);
        int tamBandeja = 4;
        int tamExaustor = rackFechado ? 2 : 0;
        int unidadesRack = 1;
        int tamParcialRack = (int) Math.ceil((numOrganizadoresFrontais * 2 + tamBandeja + tamExaustor) * 1.5);
        System.out.println((numOrganizadoresFrontais * 2 + tamBandeja + tamExaustor) * 1.5);
        int tamRack = 0;
        
        if (tamParcialRack < tamMinimoRack) {
            tamRack = tamMinimoRack;
        } else {
            if (tamParcialRack > tamMaximoRack) {
                unidadesRack = (int) Math.ceil((double) tamParcialRack / tamMaximoRack);
                tamParcialRack /= unidadesRack;
            }
            for (int i = tamMinimoRack; i <= tamMaximoRack; i += 4) {
                if (i >= tamParcialRack) {
                    tamRack = i;
                    break;
                }
            }
        }
        
        listaPrincipais.add(new Item("Organizadores de Cabo", "Frontal", numOrganizadoresFrontais));
        listaPrincipais.add(new Item("Bandeja", tamBandeja + "U", unidadesRack));
        listaPrincipais.add(new Item("Rack", String
                .format("%s, %dU", rackFechado ? "Fechado" : "Aberto", tamRack), unidadesRack));
        
        int numPCDados = numPatchCords,
            numPCPABX = numPPTroncos * numPortasPP,
            numPCRamais = numPPRamais * numPortasPP,
            totalPC = numPCDados + numPCPABX + numPCRamais;
        
        listaPrincipais.add(new Item("Patch Cables", "Cat " + categoria + ", 2,5m",
                String.format("%d (%d para dados (azul), \n%d para tronco/PÀBX (verde) \ne %d para ramal (amarelo)",
                    totalPC, numPCDados, numPCPABX, numPCRamais)));
        
        /* Preenchimento da lista de miscelânea: */
        
        listaMisc.add(new Item("Porca-gaiola", "-", tamRack * unidadesRack * 4));
        listaMisc.add(new Item("Abraçadeira", "Plástica (100 unids. por pacote)", unidadesRack));
        listaMisc.add(new Item("Abraçadeira", "Velcro (3m)", unidadesRack));
        listaMisc.add(new Item("Filtro de Linha", "8 tomadas", unidadesRack)); // considerar switches
        listaMisc.add(new Item("Etiquetas", "Patch Cable", totalPC * 2));
        listaMisc.add(new Item("Etiquetas", "Patch Panel", totalPP * numPortasPP));
        listaMisc.add(new Item("Organizadores de Cabo", "Lateral, " + tamRack + "U", unidadesRack * 2));
        listaMisc.add(new Item("Kit de Rodízio", "-", unidadesRack * 2 / 2 + 0));
        
        principaisTable.setItems(listaPrincipais);
        miscelaneaTable.setItems(listaMisc);
        
    }
    
    @FXML
    private void voltarHandler() {
        getModalStage().close();
    }
    
}
