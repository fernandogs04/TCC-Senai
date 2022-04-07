/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.usuario;

import dao.DAO_Usuario;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Usuario;
import projetotcc.admin.emprestimo.AdminEditarEmprestimoController;
import projetotcc.admin.emprestimo.AdminFazerEmprestimoController;
import projetotcc.admin.reserva.AdminEditarReservaController;
import projetotcc.admin.reserva.AdminFazerReservaController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminBuscarUsuarioController implements Initializable {
    
    //Stage
    @FXML public static Stage StageAdminBuscarUsuario;
    
    @FXML AnchorPane ap;
    
    //TableView
    //Vai receber os dados do banco de dados !
    @FXML TableView <Usuario> Tabela_Principal;
    
    //TableColumn
    @FXML TableColumn <Usuario, String> Tabela_Nome;
    @FXML TableColumn <Usuario, String> Tabela_CPF;
    @FXML TableColumn <Usuario, String> Tabela_Email;
    @FXML TableColumn <Usuario, Boolean> Tabela_Funcionario;
    
    //TextFields
    @FXML TextField tf_procurar;
    
    //Buttons
    @FXML Button btn_excluir;
    @FXML Button btn_voltar;
    @FXML Button btn_selecionar;
    
    //Checkbox
    @FXML private CheckBox cb_Funcionario;
    
    //ObservableList
    @FXML private ObservableList <Usuario> Usuario_OBList;
    
    //DAO
    @FXML DAO_Usuario DAO = new DAO_Usuario();
    
    //Existe um atributo chamado Aviao_Selecionado
    @FXML public static Usuario Usuario_Selecionado;
    
    //Se ele é um popUp ou não
    public static boolean popUp;
    
    //Se ele foi iniciador pelo fazerEmprestimos ou fazerReservas
    public static String inicializador;
    
    @FXML
    public void FazerCadastro (ActionEvent event) throws Exception {
        AdminFazerUsuarioController telaFazerCadastro = new AdminFazerUsuarioController();
        telaFazerCadastro.start(new Stage());
        
        Usuario_OBList.clear();
        
        Carrega_Tabela();
        Procurar();
    }
    
    @FXML
    public void Editar (ActionEvent event) throws Exception {
        if (Usuario_Selecionado != null) {
            AdminEditarUsuarioController telaEditar = new AdminEditarUsuarioController();
            telaEditar.start(new Stage());

            Usuario_OBList.clear();

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
    public void Deletar (ActionEvent event) throws Exception {
        if (Usuario_Selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Deletar Usuario");
            alert.setHeaderText("Tem certeza que quer deletar o usuario?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK){
                DAO_Usuario dao = new DAO_Usuario();

                dao.Deleta_Usuario(Usuario_Selecionado);
            } 

            Usuario_OBList.clear();

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
    public void Selecionar (ActionEvent event) throws Exception {
        if (Usuario_Selecionado != null) {
            if (!inicializador.equals("Menu")) {
                Stage stage = (Stage) ap.getScene().getWindow();
                Stage owner = (Stage) stage.getOwner();
                Scene scene = owner.getScene();
                Parent root = scene.getRoot();
                TextField tf_idUsuario = (TextField) root.lookup("#tf_Usuario");
                tf_idUsuario.setText(String.valueOf(Usuario_Selecionado.getId_usuario()));
                TextField tf_nomeUsuario = (TextField) root.lookup("#tf_NomeUsuario");
                tf_nomeUsuario.setText(String.valueOf(Usuario_Selecionado.getNome()));
                
                stage.close();
            }
        }
        else {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Por favor, selecione um Usuario!");
            a.showAndWait();
        }
    }
    
    public void start(Stage stage, boolean popUp, String inicializador) throws Exception {
        //Deixar o popUp como true, para a caixa de selecionar aparecer
        this.popUp = popUp;
        this.inicializador = inicializador;
        
        Parent root = FXMLLoader.load(getClass().getResource("AdminBuscarUsuario.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        
        //Veriicar quem inicializou e definir como dono
        switch (inicializador) {
            case "Reserva":
                stage.initOwner(AdminFazerReservaController.StageAdminFazerReserva);
                break;
            case "ReservaEditar":
                stage.initOwner(AdminEditarReservaController.StageAdminEditarReserva);
                break;
            case "Emprestimo":
                stage.initOwner(AdminFazerEmprestimoController.StageAdminFazerEmprestimo);
                break;
            case "EmprestimoEditar":
                stage.initOwner(AdminEditarEmprestimoController.StageAdminEditarEmprestimo);
                break;
            default:
                break;
        }
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Buscar Usuario");
        stage.show();
        
        StageAdminBuscarUsuario = stage;
    }
    
    @FXML public void Carrega_Tabela () {
        //Ele vai settar na table view os dados do banco de dados
        Tabela_Principal.getItems().clear();
        
        Tabela_Nome.setCellValueFactory(new PropertyValueFactory("nome"));
        Tabela_CPF.setCellValueFactory(new PropertyValueFactory("cpf"));
        Tabela_Email.setCellValueFactory(new PropertyValueFactory("email"));
        Tabela_Funcionario.setCellValueFactory(new PropertyValueFactory("funcionario"));
        //A variável Aviao_OBList está recebendo do DAO
        //por meio do get_Aviao() os dados do banco de dados,
        //e settando-os no atributo Tabela_Principal
        
        Usuario_OBList = DAO.get_Usuarios();
        Tabela_Principal.setItems(Usuario_OBList); 
    }     
    
    @FXML public void Procurar () {
        //Carrega os dados de forma automática e mostra na tela
        //sem a necessidade de um botão para Procurar ou Pesquisar
        ObservableList<Usuario> Func = FXCollections.observableArrayList();
        
        for (Usuario Usuario_OBList1 : Usuario_OBList) {
            //A avirável Aviao_OBlist está carregando na TextField
            //tf_procurar o que o usuario diigtou.
            if (Usuario_OBList1.getNome().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                && Usuario_OBList1.isFuncionario().equals("Sim") == cb_Funcionario.isSelected()) {
                Func.add(Usuario_OBList1);
            }
        }
        Tabela_Principal.setItems(Func);        
    }
    
    @Override
    public void initialize (URL url, ResourceBundle rb) {
        Carrega_Tabela();
        Procurar();
        
        btn_selecionar.setVisible(popUp);
        
        //O listener é quem vai ler o que foi selecionado pelo mouse com o método selectedItemProperty()
        //e vai atribuir a variável Tabela_Principal
        Tabela_Principal.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null){
                   AdminBuscarUsuarioController.Usuario_Selecionado = (Usuario) newValue;
                }
                else {
                   AdminBuscarUsuarioController.Usuario_Selecionado = null;
                }
             }
         });
         //Vai ser associado ao campo tf_procurar para buscar os dados no banco de dados
         
        tf_procurar.setOnKeyReleased((KeyEvent e)->{
            Procurar();
        });
        
        cb_Funcionario.setOnAction((ActionEvent e)->{
            Procurar();
        });
    }
}
