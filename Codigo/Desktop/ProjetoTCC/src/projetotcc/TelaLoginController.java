package projetotcc;

import dao.DAO_Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Usuario;
import projetotcc.admin.AdminMenuPrincipalController;

public class TelaLoginController implements Initializable {
    
    //TextField
    @FXML private TextField tf_cpf;
    
    //PasswordField
    @FXML private PasswordField pf_senha;
    
    //Button
    @FXML private Button btnLogin;
    
    //ObservableList
    ObservableList Usuarios_OBList;
    
    //Stage
    @FXML public static Stage StageTelaLogin;
    
    @FXML
    private void FazerLogin (ActionEvent event) throws Exception {
        if (tf_cpf.getText().isEmpty() || pf_senha.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Campo Vazio!");
            alert.setHeaderText("Favor preencher todos os campos!");
            alert.showAndWait();
        }
        else {
            DAO_Usuario dao = new DAO_Usuario();
            
            ArrayList<Usuario> resultado = dao.get_UsuariosLogin(tf_cpf.getText(), pf_senha.getText());
            
            if (resultado.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Falha no Login!");
                alert.setHeaderText("Usuario ou senha errados");
                alert.showAndWait();
            }
            else if (!resultado.get(0).isFuncionario().equals("Sim")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Falha no Login!");
                alert.setHeaderText("Conta de usuário detectada");
                alert.setContentText("Favor acessar pelo navegador");
                alert.showAndWait();
            }
            else {
                AdminMenuPrincipalController telaAdminPrincipal = new AdminMenuPrincipalController();
                Stage stage = new Stage();
                telaAdminPrincipal.start(stage);

                StageTelaLogin.close();
            }
        }
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TelaLogin.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Tela Login");
        stage.show();
        
        StageTelaLogin = stage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_cpf.textProperty().addListener((observable, antigo, novo) -> {
            //Bloquear letras
            if (!novo.matches(".*[a-zA-Z].*")) {
                //Caso o valor novo seja maior que o antigo, ou seja, uma tecla esteja sendo digitada, fazer
                if (!novo.isEmpty() && novo.length() > antigo.length()) {
                    //Precisa ser if, para evitar abuso de control c control v
                    //Se tiver 3 digitos, adicionar ponto
                    //123 -> 123.
                    if (novo.length() == 3) {
                        tf_cpf.setText(novo + ".");
                        tf_cpf.end();
                    }
                    //Se tiver 7 digitos, adicionar ponto
                    //123.456 -> 123.456.
                    else if (novo.length() == 7) {
                        tf_cpf.setText(novo + ".");
                        tf_cpf.end();
                    }
                    //Se tiver 11 digitos, adicionar traço
                    //123.456.789 -> 123.456.789-
                    else if (novo.length() == 11) {
                        tf_cpf.setText(novo + "-");
                        tf_cpf.end();
                    }
                    //Se tiver mais que 14 digitos, substituir pelo valor antigo, pois excedeu o tamanho
                    else if (novo.length() > 14) {
                        tf_cpf.setText(antigo);
                    }
                }
            }
            else {
                tf_cpf.setText(antigo);
            }
        });
    }
}
