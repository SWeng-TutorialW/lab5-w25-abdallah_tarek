package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);

        client = SimpleClient.getClient();
        client.openConnection();
        client.sendToServer("new player");

        scene = new Scene(loadFXML("pom"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("XO Game Client");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        EventBus.getDefault().unregister(this);
        try {
            client.sendToServer("remove client");
            client.closeConnection();
        } catch (IOException e) {
            System.err.println("Error during client disconnection: " + e.getMessage());
        }
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString())
            );
            alert.show();
        });
    }

    @Subscribe
    public void onGameUpdateEvent(GameUpdateEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION, "Game Update: " + event.getMessage());
            alert.show();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
