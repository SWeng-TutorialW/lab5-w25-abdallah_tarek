package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CurrentStatusB implements Serializable {
    private char[][] board;
    private char currentPlayer;
    private char self;
    public CurrentStatusB(char[][] board, char currentPlayer, char selfPlayer) {
        this.board = copyBoard(board);
        this.currentPlayer = currentPlayer;
        this.self = selfPlayer;
    }
    public char[][] getBoard() { return board; }
    public char getCurrentPlayer() { return currentPlayer; }
    public char getSelf() { return self; }

    private char[][] copyBoard(char[][] source) {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, 3);
        }
        return copy;
    }
}

