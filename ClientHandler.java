import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {

    BufferedReader in;
    PrintWriter out;
    Socket clientSocket;
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message = in.readLine();

            while(message != null) {
                out.println(message+"recieved");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
