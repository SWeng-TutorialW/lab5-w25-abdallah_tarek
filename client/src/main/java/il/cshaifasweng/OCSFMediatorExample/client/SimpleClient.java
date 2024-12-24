package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;

public class SimpleClient extends AbstractClient {
	private static SimpleClient client = null;
	private Object Platform;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String message = msg.toString();

		if (message.startsWith("warning:")) {
			EventBus.getDefault().post(new WarningEvent(new Warning(message.substring(8))));
		} else if (message.startsWith("turn:") || message.startsWith("move:") || message.startsWith("win:") || message.equals("draw")) {
			EventBus.getDefault().post(new GameUpdateEvent(message));
		} else if (message.equals("Waiting for another player...")) {
			/*Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
				alert.setTitle("Game Status");
				alert.show();
			});*/
		} else {
			System.err.println("Unexpected message received: " + message);
		}
	}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}
}
