/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.usuario;

import dao.DAO_Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Usuario;
import projetotcc.admin.AdminMenuPrincipalController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminFazerUsuarioController implements Initializable {

    //TextField
    @FXML private TextField tf_Nome;
    @FXML private TextField tf_CPF;
    @FXML private TextField tf_Email;
    @FXML private TextField tf_Senha;
    
    //Checkbox
    @FXML private CheckBox cb_Funcionario;
    
    //Stage
    @FXML public static Stage StageAdminFazerCadastro;
    
    @FXML
    public void RegistrarCadastro (ActionEvent event) throws Exception {
        if (tf_Nome.getText().isEmpty() || tf_CPF.getText().isEmpty() ||
            tf_Email.getText().isEmpty() || tf_Senha.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Campo Vazio!");
            alert.setHeaderText("Favor preencher todos os campos!");
            alert.showAndWait();
        }
        else {
            try {
                Usuario usuario = new Usuario();
                
                usuario.setNome(tf_Nome.getText());
                usuario.setCpf(tf_CPF.getText());
                usuario.setEmail(tf_Email.getText());
                usuario.setSenha(tf_Senha.getText());
                usuario.setFuncionario(cb_Funcionario.isSelected() ? "Sim" : "Nao");
                
                DAO_Usuario daoo = new DAO_Usuario();
                daoo.Insere_Cadastro(usuario);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Usuario Registrado!");
                alert.setHeaderText("Usuario registrado com sucesso!!");
                alert.showAndWait();
                
                StageAdminFazerCadastro.close();
            }
            catch (Exception ex) {
                System.out.println("Registrar - " + ex.getMessage());
            }
        }
    }
    
    @FXML
    public void Voltar (ActionEvent event) throws Exception {
        StageAdminFazerCadastro.close();
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminFazerUsuario.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(AdminMenuPrincipalController.StageAdminMenuPrincipal);
        
        stage.setScene(new Scene(root));
        stage.setTitle("Registrar Usuario");
        StageAdminFazerCadastro = stage;
        
        stage.showAndWait();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_CPF.textProperty().addListener((observable, antigo, novo) -> {
            //Bloquear letras
            if (!novo.matches(".*[a-zA-Z].*")) {
                //Caso o valor novo seja maior que o antigo, ou seja, uma tecla esteja sendo digitada, fazer
                if (!novo.isEmpty() && novo.length() > antigo.length()) {
                    //Precisa ser if, para evitar abuso de control c control v
                    //Se tiver 3 digitos, adicionar ponto
                    //123 -> 123.
                    if (novo.length() == 3) {
                        tf_CPF.setText(novo + ".");
                        tf_CPF.end();
                    }
                    //Se tiver 7 digitos, adicionar ponto
                    //123.456 -> 123.456.
                    else if (novo.length() == 7) {
                        tf_CPF.setText(novo + ".");
                        tf_CPF.end();
                    }
                    //Se tiver 11 digitos, adicionar traço
                    //123.456.789 -> 123.456.789-
                    else if (novo.length() == 11) {
                        tf_CPF.setText(novo + "-");
                        tf_CPF.end();
                    }
                    //Se tiver mais que 14 digitos, substituir pelo valor antigo, pois excedeu o tamanho
                    else if (novo.length() > 14) {
                        tf_CPF.setText(antigo);
                    }
                }
            }
            else {
                tf_CPF.setText(antigo);
            }
        });
    }
}
