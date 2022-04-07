/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.emprestimo;

import dao.DAO_Emprestimo;
import java.net.URL;
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
import static projetotcc.admin.emprestimo.AdminBuscarEmprestimoController.Emprestimo_Selecionado;
import projetotcc.admin.material.AdminBuscarMaterialController;
import projetotcc.admin.usuario.AdminBuscarUsuarioController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminEditarEmprestimoController implements Initializable {
    
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
    @FXML public static Stage StageAdminEditarEmprestimo;
    
    @FXML
    public void Voltar (ActionEvent event) throws Exception {
        StageAdminEditarEmprestimo.close();
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
                Emprestimo emprestimo = new Emprestimo();
                
                emprestimo.setId_emprestimo(Emprestimo_Selecionado.getId_emprestimo());
                emprestimo.setId_material(Integer.valueOf(tf_Material.getText()));
                emprestimo.setId_usuario(Integer.valueOf(tf_Usuario.getText()));
                emprestimo.setData_retiro(dp_dataRetiro.getValue());
                emprestimo.setDias(s_dias.getValue());
                emprestimo.setDevolvido(cb_Devolvido.isSelected() ? "Sim" : "Nao");
                
                DAO_Emprestimo daoo = new DAO_Emprestimo();
                daoo.Atualiza_Emprestimo(emprestimo);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Emprestimo Atualizado!");
                alert.setHeaderText("Emprestimo atualizado com sucesso!!");
                alert.showAndWait();
                
                StageAdminEditarEmprestimo.close();
            }
            catch (Exception ex) {
                System.out.println("Registrar - " + ex.getMessage());
            }
        }
    }
    
    @FXML
    public void BuscarUsuario (ActionEvent event) throws Exception {
        AdminBuscarUsuarioController telaFazerCadastro = new AdminBuscarUsuarioController();
        telaFazerCadastro.start(new Stage(), true, "EmprestimoEditar");
    }
    
    @FXML
    public void BuscarMaterial (ActionEvent event) throws Exception {
        AdminBuscarMaterialController telaFazerCadastro = new AdminBuscarMaterialController();
        telaFazerCadastro.start(new Stage(), true, "EmprestimoEditar");
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminEditarEmprestimo.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(AdminMenuPrincipalController.StageAdminMenuPrincipal);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Atualizar Empréstimo");
        StageAdminEditarEmprestimo = stage;
        
        stage.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);

        valueFactory.setValue(1);

        s_dias.setValueFactory(valueFactory);
        
        tf_Material.setText(Integer.toString(Emprestimo_Selecionado.getId_material()));
        tf_NomeMaterial.setText(Emprestimo_Selecionado.getNomeMaterial());
        tf_Usuario.setText(Integer.toString(Emprestimo_Selecionado.getId_usuario()));
        tf_NomeUsuario.setText(Emprestimo_Selecionado.getNomeUsuario());
        s_dias.getValueFactory().setValue(Emprestimo_Selecionado.getDias());
        dp_dataRetiro.setValue(Emprestimo_Selecionado.getData_retiro());
        cb_Devolvido.setSelected(Emprestimo_Selecionado.isDevolvido().equals("Sim"));
        
    }
    
}
