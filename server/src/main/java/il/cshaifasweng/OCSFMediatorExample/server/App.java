package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        SimpleServer server = new SimpleServer(3000);
        server.listen();
        System.out.println("Tic-Tac-Toe Server started");
    }
}
