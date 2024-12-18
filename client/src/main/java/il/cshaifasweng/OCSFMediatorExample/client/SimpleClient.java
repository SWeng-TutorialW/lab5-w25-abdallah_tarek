package il.cshaifasweng.OCSFMediatorExample.client;

import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	private static SimpleClient client = null;
	private GameStatusListener gameStatusListener;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String message = msg.toString();
		if (gameStatusListener != null) {
			gameStatusListener.updateStatus(message);
		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	public void registerGameStatusListener(GameStatusListener listener) {
		this.gameStatusListener = listener;
	}

	public interface GameStatusListener {
		void updateStatus(String status);
	}
}
