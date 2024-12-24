package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CurrentStatusB;
import il.cshaifasweng.OCSFMediatorExample.entities.GameHasEnded;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import sun.misc.Signal;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof CurrentStatusB) {
			EventBus.getDefault().post(msg);
		} else if (msg instanceof GameHasEnded) {
			EventBus.getDefault().post(msg);
		} else {
			String message = msg.toString();
			if (message.equals("client added and connected")) {
				EventBus.getDefault().post(new Signal("USR2"));
			}
			System.out.println(message);
		}
	}

	public static void initializeClient(String host, int port) {
		client = new SimpleClient(host, port);
	}

	public static SimpleClient getClient() {
		if (client == null) {
			throw new IllegalStateException("Client not initialized.");
		}
		return client;
	}
	public static String number() {

		return client.getHost();
	}
}
