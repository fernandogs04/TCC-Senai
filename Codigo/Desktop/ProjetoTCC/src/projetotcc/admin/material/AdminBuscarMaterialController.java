/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.material;

import dao.DAO_Material;
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
import model.Material;
import projetotcc.admin.emprestimo.AdminEditarEmprestimoController;
import projetotcc.admin.emprestimo.AdminFazerEmprestimoController;
import projetotcc.admin.reserva.AdminEditarReservaController;
import projetotcc.admin.reserva.AdminFazerReservaController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminBuscarMaterialController implements Initializable {
    //AnchorPane
    @FXML AnchorPane ap;
    
    //Stage
    @FXML Stage StageAdminBuscarMaterial;
    
    //TableView
    //Vai receber os dados do banco de dados !
    @FXML TableView <Material> Tabela_Principal;
    
    //TableColumn
    @FXML TableColumn <Material, String> Tabela_Nome;
    @FXML TableColumn <Material, String> Tabela_Descricao;
    @FXML TableColumn <Material, String> Tabela_Disponivel;
    
    //TextFields
    @FXML TextField tf_procurar;
    
    //Buttons
    @FXML Button btn_excluir;
    @FXML Button btn_voltar;
    @FXML Button btn_selecionar;
    
    //Checkbox
    @FXML private CheckBox cb_Disponivel;
    
    //ObservableList
    @FXML private ObservableList <Material> Material_OBList;
    
    //DAO
    @FXML DAO_Material DAO = new DAO_Material();
    
    //Existe um atributo chamado Aviao_Selecionado
    @FXML public static Material Material_Selecionado;
    
    //Se ele é um popUp ou não
    public static boolean popUp;
    
    //Se ele foi iniciador pelo fazerEmprestimos ou fazerReservas
    public static String inicializador;
    
    @FXML
    public void FazerMaterial (ActionEvent event) throws Exception {
        AdminFazerMaterialController telaFazerMaterial = new AdminFazerMaterialController();
        Stage stage = new Stage();
        telaFazerMaterial.start(stage);
        
        Material_OBList.clear();
        
        Carrega_Tabela();
        Procurar();
    }
    
    @FXML
    public void Editar (ActionEvent event) throws Exception {
        if (Material_Selecionado != null) {
            AdminEditarMaterialController telaEditar = new AdminEditarMaterialController();
            Stage stage = new Stage();
            telaEditar.start(stage);

            Material_OBList.clear();

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
        if (Material_Selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Deletar Material");
            alert.setHeaderText("Tem certeza que quer deletar o material?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK){
                DAO_Material dao = new DAO_Material();

                dao.Deleta_Material(Material_Selecionado);
            } 

            Material_OBList.clear();

            Carrega_Tabela();
            Procurar();
        }
        else {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Por favor, selecione um Material!");
            a.showAndWait();
        }
    }
    
    @FXML
    public void Selecionar (ActionEvent event) throws Exception {
        if (Material_Selecionado != null) {
            Stage stage = (Stage) ap.getScene().getWindow();
            Stage owner = (Stage) stage.getOwner();
            Scene scene = owner.getScene();
            Parent root = scene.getRoot();
            TextField tf_idMaterial = (TextField) root.lookup("#tf_Material");
            tf_idMaterial.setText(String.valueOf(Material_Selecionado.getId_material()));
            TextField tf_nomeMaterial = (TextField) root.lookup("#tf_NomeMaterial");
            tf_nomeMaterial.setText(String.valueOf(Material_Selecionado.getNome()));
            stage.close();
        }
        else {
            Alert a=new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Por favor, selecione um Material!");
            a.showAndWait();
        }
    }
    
    @FXML public void Carrega_Tabela () {
        //Ele vai settar na table view os dados do banco de dados
        Tabela_Principal.getItems().clear();
        
        Tabela_Nome.setCellValueFactory(new PropertyValueFactory("nome"));
        Tabela_Descricao.setCellValueFactory(new PropertyValueFactory("descricao"));
        Tabela_Disponivel.setCellValueFactory(new PropertyValueFactory("disponivel"));
        //A variável Aviao_OBList está recebendo do DAO
        //por meio do get_Aviao() os dados do banco de dados,
        //e settando-os no atributo Tabela_Principal
        
        Material_OBList = DAO.get_Materiais();
        Tabela_Principal.setItems(Material_OBList); 
    }     
    
    @FXML public void Procurar () {
        //Carrega os dados de forma automática e mostra na tela
        //sem a necessidade de um botão para Procurar ou Pesquisar
        ObservableList<Material> Func = FXCollections.observableArrayList();
        
        for (Material Material_OBList1 : Material_OBList) {
            //A avirável Aviao_OBlist está carregando na TextField
            //tf_procurar o que o usuario diigtou.
            if (Material_OBList1.getNome().toLowerCase().contains(tf_procurar.getText().toLowerCase())
                && Material_OBList1.isDisponivel().equals("Sim") == cb_Disponivel.isSelected()) {
                Func.add(Material_OBList1);
            }
        }
        Tabela_Principal.setItems(Func);        
    }
    
    public void start(Stage stage, boolean popUp, String inicializador) throws Exception {
        //Deixar o popUp como true, para a caixa de selecionar aparecer
        this.popUp = popUp;
        this.inicializador = inicializador;
        
        Parent root = FXMLLoader.load(getClass().getResource("AdminBuscarMaterial.fxml"));
        
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
        stage.setTitle("Buscar Material");
        stage.show();
        
        StageAdminBuscarMaterial = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                   Material_Selecionado = (Material) newValue;
                }
                else {
                   Material_Selecionado = null;
                }
             }
         });
         //Vai ser associado ao campo tf_procurar para buscar os dados no banco de dados
         
        tf_procurar.setOnKeyReleased((KeyEvent e)->{
            Procurar();
        });
        
        cb_Disponivel.setOnAction((ActionEvent e)->{
            Procurar();
        });
    }    
    
}
