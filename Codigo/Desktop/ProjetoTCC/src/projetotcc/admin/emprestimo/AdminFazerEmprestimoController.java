/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.emprestimo;

import dao.DAO_Emprestimo;
import dao.DAO_Reserva;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Emprestimo;
import projetotcc.admin.AdminMenuPrincipalController;
import projetotcc.admin.material.AdminBuscarMaterialController;
import projetotcc.admin.usuario.AdminBuscarUsuarioController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminFazerEmprestimoController implements Initializable {
    
    //TextField
    @FXML private TextField tf_Material;
    @FXML private TextField tf_NomeMaterial;
    @FXML private TextField tf_Usuario;
    @FXML private TextField tf_NomeUsuario;
    
    //Spinner
    @FXML private Spinner<Integer> s_dias;
    
    //DatePicker
    @FXML private DatePicker dp_dataRetiro;
    
    //Checkbox
    @FXML private CheckBox cb_Devolvido;

    //Stage
    @FXML public static Stage StageAdminFazerEmprestimo;
    
    @FXML
    public void Voltar (ActionEvent event) throws Exception {
        StageAdminFazerEmprestimo.close();
    }
    
    @FXML
    public void RegistrarEmprestimo (ActionEvent event) throws Exception {
        if (tf_Material.getText().isEmpty() || tf_Usuario.getText().isEmpty() || dp_dataRetiro.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Campo Vazio!");
            alert.setHeaderText("Favor preencher todos os campos!");
            alert.showAndWait();
        }
        else {
            try {
                DAO_Reserva dao = new DAO_Reserva();
                ArrayList<LocalDate> diasOcupados = dao.get_DiasOcupados(LocalDate.now(), Integer.valueOf(tf_Material.getText()), s_dias.getValue());

                if (diasOcupados.isEmpty()) {
                    Emprestimo emprestimo = new Emprestimo();
                    
                    emprestimo.setId_material(Integer.valueOf(tf_Material.getText()));
                    emprestimo.setId_usuario(Integer.valueOf(tf_Usuario.getText()));
                    emprestimo.setData_retiro(dp_dataRetiro.getValue());
                    emprestimo.setDias(s_dias.getValue());
                    emprestimo.setDevolvido(cb_Devolvido.isSelected() ? "Sim" : "Nao");
                    
                    DAO_Emprestimo daoo = new DAO_Emprestimo();
                    daoo.Insere_Emprestimo(emprestimo);
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    
                    alert.setTitle("Emprestimo Registrado!");
                    alert.setHeaderText("Emprestimo registrado com sucesso!!");
                    alert.showAndWait();
                    
                    StageAdminFazerEmprestimo.close();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Dias Ocupados!");
                    alert.setHeaderText("Este material já foi reservado hoje!");
                    alert.showAndWait();
                }
            }
            catch (Exception ex) {
                System.out.println("Registrar - " + ex.getMessage());
            }
        }
    }
    
    @FXML
    public void BuscarUsuario (ActionEvent event) throws Exception {
        AdminBuscarUsuarioController telaFazerCadastro = new AdminBuscarUsuarioController();
        telaFazerCadastro.start(new Stage(), true, "Emprestimo");
    }
    
    @FXML
    public void BuscarMaterial (ActionEvent event) throws Exception {
        AdminBuscarMaterialController telaFazerCadastro = new AdminBuscarMaterialController();
        telaFazerCadastro.start(new Stage(), true, "Emprestimo");
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminFazerEmprestimo.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(AdminMenuPrincipalController.StageAdminMenuPrincipal);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Fazer Empréstimo");
        StageAdminFazerEmprestimo = stage;
        
        stage.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);

        valueFactory.setValue(1);

        s_dias.setValueFactory(valueFactory);
    }
    
}
