package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class GameHasEnded implements Serializable {
    private String result;
    public GameHasEnded(String result) { this.result = result; }
    public String getResult() { return result; }
}
