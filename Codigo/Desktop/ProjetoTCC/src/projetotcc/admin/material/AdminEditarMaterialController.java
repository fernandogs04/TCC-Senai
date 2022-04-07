/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.material;

import dao.DAO_Material;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Material;
import projetotcc.admin.AdminMenuPrincipalController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminEditarMaterialController implements Initializable {

    //TextField
    @FXML private TextField tf_Nome;
    
    //TextArea
    @FXML private TextArea ta_Descricao;
    
    //Checkbox
    @FXML private CheckBox cb_Disponivel;
    
    //Stage
    @FXML public static Stage StageAdminEditarMaterial;
    
    @FXML
    public void AlterarMaterial (ActionEvent event) throws Exception {
        //fazer a conexão ao banco bla bla
        if (tf_Nome.getText().isEmpty() || ta_Descricao.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Campo Vazio!");
            alert.setHeaderText("Favor preencher todos os campos!");
            alert.showAndWait();
        }
        else {
            try {
                Material material = new Material();
                
                material.setNome(tf_Nome.getText());
                material.setDescricao(ta_Descricao.getText());
                material.setDisponivel(cb_Disponivel.isSelected() ? "Sim" : "Nao");
                material.setId_material(AdminBuscarMaterialController.Material_Selecionado.getId_material());
                //material.setId_registrante(69);
                
                //Cria o objeto, pois precisa do construtor
                DAO_Material daoo = new DAO_Material();
                daoo.Atualiza_Material(material);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Material Alterado!");
                alert.setHeaderText("Material alterado com sucesso!!");
                alert.showAndWait();
                
                StageAdminEditarMaterial.close();
            }
            catch (Exception ex) {
                System.out.println("Inserir material - " + ex);
            }
        }
    }
    
    @FXML
    public void Voltar (ActionEvent event) throws Exception {
        StageAdminEditarMaterial.close();
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminEditarMaterial.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(AdminMenuPrincipalController.StageAdminMenuPrincipal);
        
        stage.setScene(new Scene(root));
        stage.setTitle("Alterar Material");
        StageAdminEditarMaterial = stage;
        
        stage.showAndWait();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_Nome.setText(AdminBuscarMaterialController.Material_Selecionado.getNome());
        ta_Descricao.setText(AdminBuscarMaterialController.Material_Selecionado.getDescricao());
        cb_Disponivel.setSelected(AdminBuscarMaterialController.Material_Selecionado.isDisponivel().equals("Sim"));
    }    
    
}
