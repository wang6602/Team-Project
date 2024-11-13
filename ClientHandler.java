import java.io.*;
import java.net.*;
import java.sql.Array;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    BufferedReader in;
    PrintWriter out;
    Socket clientSocket;
    DatabaseManager databaseManager = new DatabaseManager();
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message = in.readLine();
            while(message != null) {

                String request = message.split(":")[0];

                if (request.equals("GETCHATID")) {
                    String[] temp = message.split(":");
                    String output = "";
                    ArrayList<String> result = databaseManager.getChatIDs(temp[1]);
                    for (String s : result) {
                        output += s + ",";
                    }
                    out.println(output.substring(0, output.length() - 1));
                }

                if (request.equals("GETUSERINCHAT")) {
                    String[] temp = message.split(":");
                    String output = databaseManager.getUsersinChat(temp[1]);
                    out.println(output);
                }

                if (request.equals("READCHAT")) {
                    String[] temp = message.split(":");
                    String ans = "";
                    ArrayList<String> result = databaseManager.readChat(temp[1]);
                    for (String s : result) {
                        ans += s + ",";
                    }
                    out.println(ans.substring(0, ans.length() - 1));

                }
                if (request.equals("POSTTEXT")) {
                    String[] temp = message.split(":");
                    databaseManager.newText(temp[1], temp[2], temp[3]);


                }
                if (request.equals("GETUSERPROFILEPICTURE")) {
                    String[] temp = message.split(":");
                    String output = databaseManager.getUserProfilePicture(temp[1]);
                    out.println(output);
                }
                if (request.equals("CLEARUSERPROFILEPICTURE")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.clearUserProfilePicture(temp[1]);
                    out.println(output);
                }
                if (request.equals("ADDFRIEND")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.addFriend(temp[1], temp[2]);
                    out.println(output);
                }
                if (request.equals("BLOCKFRIEND")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.blockFriend(temp[1], temp[2]);
                    out.println(output);
                }

                message = in.readLine();

            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}