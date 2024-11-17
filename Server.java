import java.io.*;
import java.net.*;

/**
 * Server Class
 * <p>
 * Server Class
 * This class handles all connections with the clients and creates a new thread for each client.
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }
}
