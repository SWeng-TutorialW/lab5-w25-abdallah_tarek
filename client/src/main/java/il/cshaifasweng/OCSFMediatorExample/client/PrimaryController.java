package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PrimaryController {
	@FXML
	private Label statusLabel;

	@FXML
	void initialize() {
		SimpleClient.getClient().registerGameStatusListener((status) -> {
			javafx.application.Platform.runLater(() -> statusLabel.setText(status));
		});
	}
}
