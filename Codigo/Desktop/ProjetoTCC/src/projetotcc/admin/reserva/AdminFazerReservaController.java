/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetotcc.admin.reserva;

import dao.DAO_Reserva;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Reserva;
import projetotcc.admin.AdminMenuPrincipalController;
import projetotcc.admin.material.AdminBuscarMaterialController;
import projetotcc.admin.usuario.AdminBuscarUsuarioController;

/**
 * FXML Controller class
 *
 * @author Fernando
 */
public class AdminFazerReservaController implements Initializable {

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
    @FXML private ChoiceBox cb_Status;
    
    //Stage
    @FXML public static Stage StageAdminFazerReserva;
    
    @FXML
    public void Voltar (ActionEvent event) throws Exception {
        StageAdminFazerReserva.close();
    }
    
    @FXML
    public void RegistrarReserva (ActionEvent event) throws Exception {
        if (tf_Material.getText().isEmpty() || tf_Usuario.getText().isEmpty() || dp_dataRetiro.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Campo Vazio!");
            alert.setHeaderText("Favor preencher todos os campos!");
            alert.showAndWait();
        }
        else {
            try {
                DAO_Reserva dao = new DAO_Reserva();
                ArrayList<LocalDate> diasOcupados = dao.get_DiasOcupados(dp_dataRetiro.getValue(), Integer.valueOf(tf_Material.getText()), s_dias.getValue());

                if (diasOcupados.isEmpty()) {
                    Reserva reserva = new Reserva();
                
                    reserva.setId_material(Integer.valueOf(tf_Material.getText()));
                    reserva.setId_usuario(Integer.valueOf(tf_Usuario.getText()));
                    reserva.setData_retiro(dp_dataRetiro.getValue());
                    reserva.setDias(s_dias.getValue());
                    reserva.setStatus(cb_Status.getValue().toString());
                    
                    dao.Insere_Reserva(reserva);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Reserva Registrada!");
                    alert.setHeaderText("Reserva registrada com sucesso!!");
                    alert.showAndWait();

                    StageAdminFazerReserva.close();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Dias Ocupados!");
                    String textoAviso = "Os dias ";
                    for (int i = 0; i < diasOcupados.size(); i++) {
                        textoAviso += diasOcupados.get(i).getDayOfMonth() + "/" + diasOcupados.get(i).getMonthValue() + ", ";
                    }
                    textoAviso += "já estão reservados";
                    alert.setHeaderText(textoAviso);
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
        telaFazerCadastro.start(new Stage(), true, "Reserva");
    }
    
    @FXML
    public void BuscarMaterial (ActionEvent event) throws Exception {
        AdminBuscarMaterialController telaFazerCadastro = new AdminBuscarMaterialController();
        telaFazerCadastro.start(new Stage(), true, "Reserva");
    }
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminFazerReserva.fxml"));
        
        //Bloquear a janela principal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(AdminMenuPrincipalController.StageAdminMenuPrincipal);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Fazer Reserva");
        StageAdminFazerReserva = stage;
        
        stage.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
        valueFactory.setValue(1);
        
        s_dias.setValueFactory(valueFactory);
        
        cb_Status.setItems(FXCollections.observableArrayList("Aberto", "Cancelado", "Concluido"));
        cb_Status.setValue("Aberto");
    }   
    
}
