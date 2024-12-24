package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class update implements Serializable {
    private int row, col;
    public update(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
