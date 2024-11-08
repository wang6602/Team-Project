import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;
    private String password;
    private String userID;

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

    public String[] getChatIDs(){
        try{
            out.println("GETCHATID:" + userID);
            String[] result = in.readLine().split(",");
            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getUsersInChat(String chatID){
        try{
            out.println("GETUSERINCHAT:" + chatID);
            return(in.readLine());

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String[] readChat(String chatID){
        try{
            out.println("READCHAT:" + chatID);
            String[] result = in.readLine().split(",");
            return result;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void newText(String chatID, String message){
        try{
            out.println("POSTTEXT:" + userID + ":" + chatID + ":" + message);

        } catch (Exception e){

            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.sendMessage();
    }

}

