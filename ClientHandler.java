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

                if (message.contains("GETCHATID")) {
                    String[] temp = message.split(":");
                    String output = "";
                    ArrayList<String> result = databaseManager.getChatIDs(temp[1]);
                    for (String s : result) {
                        output += s + ",";
                    }
                    out.println(output.substring(0, output.length() - 1));
                }

                if (message.contains("GETUSERINCHAT")) {
                    String[] temp = message.split(":");
                    String output = databaseManager.getUsersinChat(temp[1]);
                    out.println(output);
                }

                if (message.contains("READCHAT")) {
                    String[] temp = message.split(":");
                    String ans = "";
                    ArrayList<String> result = databaseManager.readChat(temp[1]);
                    for (String s : result) {
                        ans += s + ",";
                    }
                    out.println(ans.substring(0, ans.length() - 1));

                }
                if (message.contains("POSTTEXT")) {
                    String[] temp = message.split(":");
                    databaseManager.newText(temp[1], temp[2], temp[3]);


                }

                message = in.readLine();

            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
