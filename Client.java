import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(){
        try {
            socket = new Socket("localhost", 4242);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        try {
            out.println("hello world");
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.sendMessage();
    }

}

