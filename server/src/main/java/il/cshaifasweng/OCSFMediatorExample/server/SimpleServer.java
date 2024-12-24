package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.CurrentStatusB;
import il.cshaifasweng.OCSFMediatorExample.entities.GameHasEnded;
import il.cshaifasweng.OCSFMediatorExample.entities.update;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

public class SimpleServer extends AbstractServer {
	private ConnectionToClient playerX;
	private ConnectionToClient playerO;
	private char turn = 'X';
	private boolean gameInProgress = false;
	private char [][] board = new char[3][3];



	public SimpleServer(int port) {
		super(port);
		resetBoard();

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof update move && gameInProgress) {
			processMove(move, client);
		} else if (msg instanceof String) {
			String msgString = msg.toString();
			System.out.println(msgString);
			if (msgString.startsWith("add player")){
				try {
					System.out.println(playerX);
					System.out.println(playerO);
					if (playerX == null) {
						playerX = client;
						client.sendToClient("client added and connected");
					} else if (playerO == null) {
						playerO = client;
						client.sendToClient("client added and connected");
					}
					if(playerX != null && playerO != null && !gameInProgress) {
						startNewGame();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(msgString.startsWith("remove player")){
				System.out.println("remove player");
				System.out.println(gameInProgress);
				if (gameInProgress) {
					try {
						if (playerX == client) {
							System.out.println("Player X ended the session");
							playerO.sendToClient(new GameHasEnded("Player X ended the session"));
						} else {
							System.out.println("Player O ended the session");
							playerX.sendToClient(new GameHasEnded("Player O ended the session"));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				gameInProgress = false;
				playerO = null;
				playerX = null;
			}
		}
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ' ';
			}
		}
		turn = 'X';
	}


	private void startNewGame() {
		gameInProgress = true;
		resetBoard();
		try {
			playerO.sendToClient(new CurrentStatusB(board, turn, 'O'));
			playerX.sendToClient(new CurrentStatusB(board, turn, 'X'));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void processMove(update move, ConnectionToClient client) {
		int row = move.getRow();
		int col = move.getCol();

		char clientSymbol = (client == playerX) ? 'X' : (client == playerO) ? 'O' : ' ';
		if (clientSymbol == turn && board[row][col] == ' ') {
			board[row][col] = turn;
			try {
				changeTurn();
				playerO.sendToClient(new CurrentStatusB(board, turn, 'O'));
				playerX.sendToClient(new CurrentStatusB(board, turn, 'X'));
				if (check_if_won())
				{
					if(turn=='X') {
						playerO.sendToClient(new GameHasEnded("Player " + 'O' + " won the game!"));
						playerX.sendToClient(new GameHasEnded("Player " + 'O' + " won the game!"));
					}
					else {
						playerX.sendToClient(new GameHasEnded("Player " +'X' + " won the game!"));
						playerO.sendToClient(new GameHasEnded("Player " +'X' + " won the game!"));
					}
					playerO = null;
					playerX = null;
					gameInProgress = false;
				}
				else if (check_for_draw()) {

					playerO.sendToClient(new GameHasEnded("draw!"));
					playerX.sendToClient(new GameHasEnded("draw!"));
					playerO = null;
					playerX = null;
					gameInProgress = false;
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private boolean check_for_draw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') {
					return false;
				}
			}
		}

		return true;
	}
	private boolean check_if_won() {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				return true;
			}
		}
		for (int j = 0; j < 3; j++) {
			if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
				return true;
			}
		}
		return board[1][1] != ' ' && ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
				(board[2][0] == board[1][1] && board[1][1] == board[0][2]));
	}



	private void changeTurn() {
		turn = (turn == 'X') ? 'O' : 'X';
	}
}
