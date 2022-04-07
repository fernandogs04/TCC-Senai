package projetotcc.admin.emprestimo;

import dao.DAO_Emprestimo;
import dao.DAO_Material;
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
import model.Emprestimo;

public class AdminBuscarEmprestimoController implements Initializable {

    //Stage
    @FXML public static Stage StageAdminBuscarEmprestimos;
    
    //TableView
    //Vai receber os dados do banco de dados !
    @FXML TableView <Emprestimo> Tabela_Principal;
    
    //TableColumn
    @FXML TableColumn <Emprestimo, String> Tabela_Usuario;
    @FXML TableColumn <Emprestimo, String> Tabela_Material;
    @FXML TableColumn <Emprestimo, LocalDate> Tabela_Data_Retiro;
    @FXML TableColumn <Emprestimo, Integer> Tabela_Dias;
    @FXML TableColumn <Emprestimo, String> Tabela_Devolvido;
    
    //TextFields
    @FXML TextField tf_procurar;
    
    //Checkbox
    @FXML private CheckBox cb_Devolvido;
    
    //ChoiceBox
    @FXML private ChoiceBox cb_EscolhaBusca;
    
    //ObservableList
    @FXML private ObservableList <Emprestimo> Emprestimo_OBList;
    
    //DAO
    @FXML DAO_Emprestimo DAO = new DAO_Emprestimo();
    
    //Existe um atributo chamado Aviao_Selecionado
    @FXML public static Emprestimo Emprestimo_Selecionado;
    
    @FXML
    public void FazerEmprestimo (ActionEvent event) throws Exception {
        AdminFazerEmprestimoController telaFazerEmprestimo = new AdminFazerEmprestimoController();
        telaFazerEmprestimo.start(new Stage());
            
        Emprestimo_OBList.clear();

        Carrega_Tabela();
        Procurar();
    }
    
    
    @FXML
    public void Editar (ActionEvent event) throws Exception {
        if (Emprestimo_Selecionado != null) {
            AdminEditarEmprestimoController telaEditar = new AdminEditarEmprestimoController();
            Stage stage = new Stage();
            telaEditar.start(stage);

            Emprestimo_OBList.clear();

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
        if (Emprestimo_Selecionado != null) {
            
            if (Emprestimo_Selecionado.isDevolvido().equals("Sim")) {
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Este empréstimo já foi devolvido!");
                a.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Marcar Devolução");
                alert.setHeaderText("Tem certeza que quer confirmar a devolução?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    DAO_Emprestimo dao = new DAO_Emprestimo();

                    dao.Marcar_Devolvido(Emprestimo_Selecionado);
                    
                    //Agora que a reserva foi concluida, pode marcar o material como disponivel
                    Alert alertaMaterial = new Alert(Alert.AlertType.CONFIRMATION);

                    alertaMaterial.setTitle("Marcar Disponibilidade");
                    alertaMaterial.setHeaderText("Deseja marcar o material como disponivel?");

                    Optional<ButtonType> resultadoMaterial = alertaMaterial.showAndWait();

                    if (resultadoMaterial.get() == ButtonType.OK){
                        DAO_Material daoMaterial = new DAO_Material ();
                        daoMaterial.Marcar_Disponivel(Emprestimo_Selecionado.getId_material());
                    }
                } 
                
                Emprestimo_OBList.clear();

                Carrega_Tabela();
                Procurar();
            }
        }
        else {
            Alert a=new Alert(Alert.AlertType.WARNING);
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
        Tabela_Devolvido.setCellValueFactory(new PropertyValueFactory("devolvido"));
        //A variável Aviao_OBList está recebendo do DAO
        //por meio do get_Aviao() os dados do banco de dados,
        //e settando-os no atributo Tabela_Principal
        
        Emprestimo_OBList = DAO.get_Emprestimos();
        Tabela_Principal.setItems(Emprestimo_OBList); 
    }     
    
    @FXML public void Procurar () {
        //Carrega os dados de forma automática e mostra na tela
        //sem a necessidade de um botão para Procurar ou Pesquisar
        ObservableList<Emprestimo> Func = FXCollections.observableArrayList();
        
        //Caso o usuario deseje procurar pelo nome
        if (cb_EscolhaBusca.getValue() == "Nome") {
            for (Emprestimo Emprestimo_OBList1 : Emprestimo_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario diigtou.
                if (Emprestimo_OBList1.getNomeUsuario().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                    && cb_Devolvido.isSelected() == Emprestimo_OBList1.isDevolvido().equals("Sim")) {
                    Func.add(Emprestimo_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
        //Caso o usuario deseje procurar pelo material
        if (cb_EscolhaBusca.getValue() == "Material") {
            for (Emprestimo Emprestimo_OBList1 : Emprestimo_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario diigtou.
                if (Emprestimo_OBList1.getNomeMaterial().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                    && cb_Devolvido.isSelected() == Emprestimo_OBList1.isDevolvido().equals("Sim")) {
                    Func.add(Emprestimo_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
        //Caso o usuario deseje procurar pela data
        if (cb_EscolhaBusca.getValue() == "Data") {
            for (Emprestimo Emprestimo_OBList1 : Emprestimo_OBList) {
                //A avirável Aviao_OBlist está carregando na TextField
                //tf_procurar o que o usuario diigtou.
                if (Emprestimo_OBList1.getData_retiro().toString().contains(tf_procurar.getText().toLowerCase())
                    && cb_Devolvido.isSelected() == Emprestimo_OBList1.isDevolvido().equals("Sim")) {
                    Func.add(Emprestimo_OBList1);
                }
            }
            Tabela_Principal.setItems(Func);
        }
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminBuscarEmprestimo.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Buscar Empréstimos");
        stage.show();
        
        StageAdminBuscarEmprestimos = stage;
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
                    Emprestimo_Selecionado = (Emprestimo) newValue;
                }
                else {
                    Emprestimo_Selecionado = null;
                }
             }
         });
         //Vai ser associado ao campo tf_procurar para buscar os dados no banco de dados
         
        tf_procurar.setOnKeyReleased((KeyEvent e)->{
            Procurar();
        });
        
        cb_Devolvido.setOnAction((ActionEvent e)->{
            Procurar();
        });
    }    
    
}
