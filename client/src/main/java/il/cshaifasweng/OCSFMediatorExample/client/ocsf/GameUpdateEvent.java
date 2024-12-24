package il.cshaifasweng.OCSFMediatorExample.client;

public class GameUpdateEvent {
    private final String message;

    public GameUpdateEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUpdateType() {
        if (message.contains(":")) {
            return message.split(":")[0];
        }
        return message;
    }

    public String getDetails() {
        if (message.contains(":")) {
            return message.split(":")[1];
        }
        return "";
    }
}
