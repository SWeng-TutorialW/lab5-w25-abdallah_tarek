package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import sun.misc.Signal;

import java.io.IOException;

public class PrimaryController {


	@FXML
	private Button playButton;

	@FXML
	private Label connectionStatusLabel;

	@FXML
	void initialize() {
		EventBus.getDefault().register(this);
		connectionStatusLabel.setText(""); // Clear the status label

	}


	@FXML
	void startGame(ActionEvent event) {
		String ip = "172.20.10.3";
		String portText = "3000";

		try {
			int port = Integer.parseInt(portText);
			SimpleClient.initializeClient(ip, port);  // Initialize the client with provided IP and port
			SimpleClient.getClient().openConnection();
			playButton.setText("Connecting...");
			connectionStatusLabel.setText("Connecting to server...");
			System.out.println(ip);
			System.out.println(port);
		}  catch (IOException e) {
			connectionStatusLabel.setText("Failed to connect! Check IP and Port.");
			e.printStackTrace();
		}
		try {
			SimpleClient.getClient().sendToServer("add player");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void whenConnected(Signal event) {
		Platform.runLater(() -> {
			playButton.setText("Waiting for another player...");
			connectionStatusLabel.setText("Connected!");
		});
	}
}
