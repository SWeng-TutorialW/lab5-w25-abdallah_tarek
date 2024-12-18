package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static SimpleClient client;

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    @Override
    public void start(Stage stage) {
        try {
            // Initialize client connection
            client = SimpleClient.getClient();
            client.openConnection();
            client.sendToServer("new player");

            // Load game UI
            Parent root = loadFXML("pom");
            scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static SimpleClient getClient() {
        return client;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
