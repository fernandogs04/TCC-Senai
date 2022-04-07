package projetotcc;

import javafx.application.Application;
import javafx.stage.Stage;

public class ProjetoTCC extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        TelaLoginController telaLogin = new TelaLoginController();
        telaLogin.start(stage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
