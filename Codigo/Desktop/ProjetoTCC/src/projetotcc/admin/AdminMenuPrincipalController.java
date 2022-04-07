package projetotcc.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import projetotcc.TelaLoginController;
import projetotcc.admin.material.AdminBuscarMaterialController;
import projetotcc.admin.usuario.AdminBuscarUsuarioController;

public class AdminMenuPrincipalController implements Initializable {
    
    //Buttons
    @FXML private Button btnEmprestimo;
    @FXML private Button btnReserva;
    @FXML private Button btnMaterial;
    @FXML private Button btnUsuario;
    @FXML private Button btnLogout;
    
    //Pane
    @FXML private Pane paneConteudo;
    
    //Stage
    @FXML public static Stage StageAdminMenuPrincipal;
        
    @FXML
    private void entrouCaixa (MouseEvent event) throws Exception {
        Button botao = (Button)event.getSource();
        botao.setStyle("-fx-background-radius: 0; -fx-background-color: #A2A2A2; -fx-border-color: #B5B5B5");
    }
    
    @FXML
    private void saiuCaixa (MouseEvent event) throws Exception {
        Button botao = (Button)event.getSource();
        botao.setStyle("-fx-background-radius: 0; -fx-background-color: #D9D9D9; -fx-border-color: #B5B5B5");
    }
    
    @FXML
    private void clicouBotao (ActionEvent event) throws Exception {
        Button botao = (Button)event.getSource();
        
        Pane novoPane;
        
        String idAba = botao.getId();
        if (idAba == null) {idAba = "";}
        switch (idAba) {
            case "btnEmprestimo":
                paneConteudo.getChildren().clear();
                novoPane = FXMLLoader.load(getClass().getResource("/projetotcc/admin/emprestimo/AdminBuscarEmprestimo.fxml"));
                paneConteudo.getChildren().add(novoPane);
                break;
            case "btnReserva":
                paneConteudo.getChildren().clear();
                novoPane = FXMLLoader.load(getClass().getResource("/projetotcc/admin/reserva/AdminBuscarReserva.fxml"));
                paneConteudo.getChildren().add(novoPane);
                break;
            case "btnMaterial":
                //Deixar como false, pois não é popUp neste contexto,
                //e o botão selecionar não deve aparecer
                AdminBuscarMaterialController.popUp = false;
                AdminBuscarMaterialController.inicializador = "Menu";
                
                paneConteudo.getChildren().clear();
                novoPane = FXMLLoader.load(getClass().getResource("/projetotcc/admin/material/AdminBuscarMaterial.fxml"));
                paneConteudo.getChildren().add(novoPane);
                break;
            case "btnUsuario":
                //Deixar como false, pois não é popUp neste contexto,
                //e o botão selecionar não deve aparecer
                AdminBuscarUsuarioController.popUp = false;
                AdminBuscarUsuarioController.inicializador = "Menu";
                
                paneConteudo.getChildren().clear();
                novoPane = FXMLLoader.load(getClass().getResource("/projetotcc/admin/usuario/AdminBuscarUsuario.fxml"));
                paneConteudo.getChildren().add(novoPane);
                break;
            case "btnLogout":
                //Deixar como false, pois não é popUp neste contexto,
                //e o botão selecionar não deve aparecer
                TelaLoginController telaLogin = new TelaLoginController();
                Stage stage = new Stage();
                telaLogin.start(stage);

                StageAdminMenuPrincipal.close();
                break;
            default:
                paneConteudo.getChildren().clear();
                break;
        }
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminMenuPrincipal.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Menu Principal");
        stage.show();
        
        StageAdminMenuPrincipal = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
