package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleServer extends AbstractServer {
	private static final ArrayList<ConnectionToClient> players = new ArrayList<>();
	private String[] board = new String[9];
	private int currentPlayerIndex = -1;

	public SimpleServer(int port) {
		super(port);
		resetBoard();
	}

	private void resetBoard() {
		for (int i = 0; i < 9; i++) {
			board[i] = "";
		}
	}

	@Override
	protected synchronized void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String message = msg.toString();

		try {
			if (message.equals("new player")) {
				if (players.size() < 2) {
					players.add(client);
					client.sendToClient("Waiting for another player...");

					if (players.size() == 2) {
						Collections.shuffle(players);
						players.get(0).sendToClient("You are X");
						players.get(1).sendToClient("You are O");
						currentPlayerIndex = 0;
						players.get(currentPlayerIndex).sendToClient("Your turn");
					}
				} else {
					client.sendToClient("Game is full!");
				}
			} else if (message.startsWith("move:")) {
				if (client == players.get(currentPlayerIndex)) {
					String[] parts = message.split(":");
					int position = Integer.parseInt(parts[1]);

					if (position >= 0 && position < 9 && board[position].isEmpty()) {
						board[position] = currentPlayerIndex == 0 ? "X" : "O";

						if (checkWin()) {
							client.sendToClient("You win!");
							players.get(1 - currentPlayerIndex).sendToClient("You lose!");
							resetBoard();
						} else if (isBoardFull()) {
							players.forEach(p -> {
								try {
									p.sendToClient("It's a draw!");
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
							resetBoard();
						} else {
							currentPlayerIndex = 1 - currentPlayerIndex;
							players.get(currentPlayerIndex).sendToClient("Your turn");
						}
					} else {
						client.sendToClient("Invalid move. Try again.");
					}
				} else {
					client.sendToClient("Not your turn.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkWin() {
		String[][] winPatterns = {
				{board[0], board[1], board[2]},
				{board[3], board[4], board[5]},
				{board[6], board[7], board[8]},
				{board[0], board[3], board[6]},
				{board[1], board[4], board[7]},
				{board[2], board[5], board[8]},
				{board[0], board[4], board[8]},
				{board[2], board[4], board[6]},
		};

		for (String[] pattern : winPatterns) {
			if (!pattern[0].isEmpty() && pattern[0].equals(pattern[1]) && pattern[1].equals(pattern[2])) {
				return true;
			}
		}

		return false;
	}

	private boolean isBoardFull() {
		for (String cell : board) {
			if (cell.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
