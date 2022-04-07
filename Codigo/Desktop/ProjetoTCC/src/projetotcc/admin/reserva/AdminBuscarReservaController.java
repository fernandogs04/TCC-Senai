package projetotcc.admin.reserva;

import dao.DAO_Material;
import dao.DAO_Reserva;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Reserva;

public class AdminBuscarReservaController implements Initializable {

    //Stage
    @FXML public static Stage StageAdminBuscarReserva;
    
    //TableView
    //Vai receber os dados do banco de dados !
    @FXML TableView <Reserva> Tabela_Principal;
    
    //TableColumn
    @FXML TableColumn <Reserva, String> Tabela_Usuario;
    @FXML TableColumn <Reserva, String> Tabela_Material;
    @FXML TableColumn <Reserva, LocalDate> Tabela_Data_Retiro;
    @FXML TableColumn <Reserva, Integer> Tabela_Dias;
    @FXML TableColumn <Reserva, String> Tabela_Status;
    
    //TextFields
    @FXML TextField tf_procurar;
    
    //Checkbox
    @FXML private CheckBox cb_Aberto;
    
    //ChoiceBox
    @FXML private ChoiceBox cb_EscolhaBusca;
    
    //ObservableList
    @FXML private ObservableList <Reserva> Reserva_OBList;
    
    //DAO
    @FXML DAO_Reserva DAO = new DAO_Reserva();
    
    //Existe um atributo chamado Aviao_Selecionado
    @FXML public static Reserva Reserva_Selecionada;
    
    @FXML
    public void FazerReserva (ActionEvent event) throws Exception {
        AdminFazerReservaController telaFazerEmprestimo = new AdminFazerReservaController();
        Stage stage = new Stage();
        telaFazerEmprestimo.start(stage);
            
        Reserva_OBList.clear();

        Carrega_Tabela();
        Procurar();
    }
    
    @FXML
    public void Editar (ActionEvent event) throws Exception {
        if (Reserva_Selecionada != null) {
            AdminEditarReservaController telaEditar = new AdminEditarReservaController();
            Stage stage = new Stage();
            telaEditar.start(stage);

            Reserva_OBList.clear();

            Carrega_Tabela();
            Procurar();
        }
        else {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Por favor, selecione uma Usuário!");
            a.showAndWait();
        }
    }
    
    
    @FXML
    public void MarcarDevolucao (ActionEvent event) throws Exception {
        if (Reserva_Selecionada != null) {
            //Não pode marcar a devolução, pois já foi concluica/cancelada
            if (Reserva_Selecionada.getStatus() == "Concluido") {
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Esta reserva já foi concluida!");
                a.showAndWait();
            }
            if (Reserva_Selecionada.getStatus() == "Cancelada") {
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Esta reserva foi cancelada!");
                a.showAndWait();
            }
            else {
                //Confirmação se quer fazer a devolução
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Marcar Devolução");
                alert.setHeaderText("Tem certeza que quer confirmar a devolução?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    DAO_Reserva dao = new DAO_Reserva();
                    dao.Marcar_Devolvido(Reserva_Selecionada);
                    
                    //Agora que a reserva foi concluida, pode marcar o material como disponivel
                    Alert alertaMaterial = new Alert(Alert.AlertType.CONFIRMATION);

                    alertaMaterial.setTitle("Marcar Disponibilidade");
                    alertaMaterial.setHeaderText("Deseja marcar o material como disponivel?");

                    Optional<ButtonType> resultadoMaterial = alertaMaterial.showAndWait();

                    if (resultadoMaterial.get() == ButtonType.OK){
                        DAO_Material daoMaterial = new DAO_Material ();
                        daoMaterial.Marcar_Disponivel(Reserva_Selecionada.getId_material());
                    }
                } 
                
                Reserva_OBList.clear();

                Carrega_Tabela();
                Procurar();
            }
        }
        else {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Por favor, selecione uma Usuário!");
            a.showAndWait();
        }
    }
    
    @FXML public void Carrega_Tabela () {
        //Ele vai settar na table view os dados do banco de dados
        Tabela_Principal.getItems().clear();
        
        Tabela_Usuario.setCellValueFactory(new PropertyValueFactory("nomeUsuario"));
        Tabela_Material.setCellValueFactory(new PropertyValueFactory("nomeMaterial"));
        Tabela_Data_Retiro.setCellValueFactory(new PropertyValueFactory("data_retiro"));
        Tabela_Dias.setCellValueFactory(new PropertyValueFactory("dias"));
        Tabela_Status.setCellValueFactory(new PropertyValueFactory("status"));
        //A variável Aviao_OBList está recebendo do DAO
        //por meio do get_Aviao() os dados do banco de dados,
        //e settando-os no atributo Tabela_Principal
        
        Reserva_OBList = DAO.get_Reservas();
        Tabela_Principal.setItems(Reserva_OBList); 
    }
    
    @FXML public void Procurar () {
        //Carrega os dados de forma automática e mostra na tela
        //sem a necessidade de um botão para Procurar ou Pesquisar
        ObservableList<Reserva> Func = FXCollections.observableArrayList();
        
        //Caso o usuario deseje procurar pelo nome
        if (cb_EscolhaBusca.getValue() == "Nome") {
            for (Reserva Reserva_OBList1 : Reserva_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario digitou.
                if (Reserva_OBList1.getNomeUsuario().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                    && cb_Aberto.isSelected() == Reserva_OBList1.getStatus().equals("Aberto")) {
                    Func.add(Reserva_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
        //Caso o usuario deseje procurar pelo material
        if (cb_EscolhaBusca.getValue() == "Material") {
            for (Reserva Reserva_OBList1 : Reserva_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario digitou.
                if (Reserva_OBList1.getNomeMaterial().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                    && cb_Aberto.isSelected() == Reserva_OBList1.getStatus().equals("Aberto")) {
                    Func.add(Reserva_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
        //Caso o usuario deseje procurar pela data
        if (cb_EscolhaBusca.getValue() == "Data") {
            for (Reserva Reserva_OBList1 : Reserva_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario digitou.
                if (Reserva_OBList1.getData_retiro().toString().contains(tf_procurar.getText().toLowerCase())
                    && cb_Aberto.isSelected() == Reserva_OBList1.getStatus().equals("Aberto")) {
                    Func.add(Reserva_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminBuscarReserva.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Buscar Reserva");
        stage.show();
        
        StageAdminBuscarReserva = stage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb_EscolhaBusca.setItems(FXCollections.observableArrayList("Nome", "Material", "Data"));
        cb_EscolhaBusca.setValue("Nome");
        
        Carrega_Tabela();
        Procurar();
        
        //O listener é quem vai ler o que foi selecionado pelo mouse com o método selectedItemProperty()
        //e vai atribuir a variável Tabela_Principal
        Tabela_Principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null){
                    Reserva_Selecionada = (Reserva) newValue;
                }
                else {
                    Reserva_Selecionada = null;
                }
             }
        });
        
        //Vai ser associado ao campo tf_procurar para buscar os dados no banco de dados 
        tf_procurar.setOnKeyReleased((KeyEvent e)->{
            Procurar();
        });
        
        cb_Aberto.setOnAction((ActionEvent e)->{
            Procurar();
        });
    }    
    
}
