package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class PrimaryController {

	@FXML
	private Label statusLabel;

	@FXML
	private GridPane gameGrid;

	private String currentPlayer; // "X" or "O"
	private boolean isPlayerTurn;
	private String[][] board = new String[3][3];

	@FXML
	public void initialize() {
		resetGame();

		// Register to EventBus to listen for server updates
		EventBus.getDefault().register(this);
	}

	@FXML
	private void handleMove(javafx.event.ActionEvent event) {
		Button clickedButton = (Button) event.getSource();

		if (!isPlayerTurn || !clickedButton.getText().isEmpty()) {
			return;
		}

		int row = GridPane.getRowIndex(clickedButton);
		int col = GridPane.getColumnIndex(clickedButton);

		board[row][col] = currentPlayer;
		clickedButton.setText(currentPlayer);
		isPlayerTurn = false;

		try {
			SimpleClient.getClient().sendToServer("move:" + row + "," + col);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleRestart() {
		try {
			SimpleClient.getClient().sendToServer("restart");
		} catch (IOException e) {
			e.printStackTrace();
		}
		resetGame();
	}

	private void resetGame() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = "";
				Button button = getNodeByRowColumnIndex(i, j);
				if (button != null) {
					button.setText("");
				}
			}
		}
		statusLabel.setText("Waiting for players...");
		isPlayerTurn = false;
	}

	@Subscribe
	public void handleGameUpdate(GameUpdateEvent event) {
		String update = event.getMessage();
		Platform.runLater(() -> {
			if (update.startsWith("turn:")) {
				currentPlayer = update.split(":")[1];
				isPlayerTurn = true;
				statusLabel.setText("Your turn as " + currentPlayer);
			} else if (update.startsWith("move:")) {
				String[] parts = update.split(":")[1].split(",");
				int row = Integer.parseInt(parts[0]);
				int col = Integer.parseInt(parts[1]);
				String player = parts[2];

				board[row][col] = player;
				Button button = getNodeByRowColumnIndex(row, col);
				if (button != null) {
					button.setText(player);
				}
			} else if (update.startsWith("win:")) {
				statusLabel.setText(update.split(":")[1] + " wins!");
			} else if (update.equals("draw")) {
				statusLabel.setText("It's a draw!");
			}
		});
	}

	private Button getNodeByRowColumnIndex(int row, int col) {
		for (javafx.scene.Node node : gameGrid.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				return (Button) node;
			}
		}
		return null;
	}

	@FXML
	public void cleanup() {
		// Unregister from EventBus
		EventBus.getDefault().unregister(this);
	}
}
