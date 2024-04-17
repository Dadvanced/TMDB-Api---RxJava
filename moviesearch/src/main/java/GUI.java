import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("MainView.fxml");

        if (fxmlUrl == null) {
            throw new RuntimeException("No se pudo encontrar el archivo FXML: MainView.fxml");
        }
        Parent root = FXMLLoader.load(fxmlUrl);

        primaryStage.setTitle("Mi Aplicación de Búsqueda de Películas"); // stage = window application
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }
}
