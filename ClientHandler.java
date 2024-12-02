
import java.io.*;
import java.net.*;
import java.sql.Array;
import java.util.ArrayList;

/**
 * ClientHandler Class
 * <p>
 * This Client Handler class performs all the processing from the server side of our application
 * <p>
 * We created an instance of the DatabaseManager class to use its methods according to the inputs from
 * the user.
 * <p>
 * We used if-statements to find a "key word" from the Client input, then performed the operation
 * based off the "key word" accordingly.
 * <p>
 * If the method is not void, we used a PrintWriter to return the data back to the Client class.
 * <p>
 * The processing itself is carried out in the ClientHandler class
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project</p>
 *
 * @author Alan Wang, Rohit Sattuluri, Sophia Zakar, Tatjana Trajkovic, BLK
 * @version November 16, 2024
 */



public class ClientHandler implements Runnable, ClientHandlerInterface {

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
            while (message != null) {

                String request = message.split(":")[0];

                if (request.equals("GETCHATID")) {
                    String[] temp = message.split(":");
                    String output = "";
                    ArrayList<String> result = databaseManager.getChatIDs(temp[1]);
                    for (String s : result) {
                        output += s + ",";
                    }
                    if(output.length()== 0){
                        out.println("null");
                    } else {
                        out.println(output.substring(0, output.length() - 1));
                    }
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
                    if(result == null){
                        out.println("null");
                    } else {
                        out.println(ans.substring(0, ans.length() - 1));
                    }

                }
                if (request.equals("POSTTEXT")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.newText(temp[1], temp[2], temp[3]);
                    out.println(output);
                }
                if (request.equals("GETUSERPROFILEPICTURE")) {
                    String[] temp = message.split(":");
                    String output = databaseManager.getUserProfilePicture(temp[1]);
                    out.println(output);
                }
                if (request.equals("UPDATEUSERPROFILEPICTURE")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.updateUserProfilePicture(temp[1],temp[2]);
                    out.println(output);
                }
                if (request.equals("CLEARUSERPROFILEPICTURE")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.clearUserProfilePicture(temp[1]);
                    out.println(output);
                }
                if (request.equals("GETFRIENDS")) {
                    String[] temp = message.split(":");
                    String output = databaseManager.getFriends(temp[1]);
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

                if (request.equals("DELETETEXT")) {
                    String[] temp = message.split(":");
                    databaseManager.deleteText(temp[1], Integer.parseInt(temp[2]));
                }

                if (request.equals("CREATECHAT")) {
                    String[] temp = message.split(":");
                    String[] userID = temp[1].split(",");
                    String newChat = databaseManager.createChat(userID);
                    out.println(newChat);
                }

                if (request.equals("CREATEUSER")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.createUser(temp[1], temp[2]);
                    out.println(output);
                }

                if (request.equals("REMOVEUSER")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.removeUser(temp[1]);
                    out.println(output);
                }

                if (request.equals("REMOVEUSERFROMCHAT")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.removeUserFromChat(temp[1], temp[2]);
                    out.println(output);
                }

                // Tatjana's methods
                if (request.equals("ADDUSERTOCHAT")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.addUserToChat(temp[1], temp[2]);
                    out.println(output);
                }

                if (request.equals("USERLOOKUP")) {
                    String[] temp = message.split(":");
                    String output = "";
                    ArrayList<User> result = databaseManager.userLookup(temp[1]);
                    for (User s : result) {
                        output += s.getUsername() + ",";
                    }
                    out.println(output.substring(0, output.length() - 1));
                }

                if (request.equals("USERVIEWER")) {
                    String[] temp = message.split(":");
                    String output = "";
                    ArrayList<String> result = databaseManager.userViewer();
                    for (String s : result) {
                        output += s + ",";
                    }
                    out.println(output.substring(0, output.length() - 1));
                }

                if (request.equals("UPDATEUSER")) {
                    try {
                        String[] temp = message.split(":");
                        String username = temp[1];
                        String password = temp[2];
                        User user = new User(username, password);
                        databaseManager.updateUser(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.println("ERROR");
                    }
                }


                if (request.equals("LOGINUSER")) {
                    String[] temp = message.split(":");
                    boolean output = databaseManager.loginUser(temp[1], temp[2], temp[3]);
                    out.println(output);
                }


                message = in.readLine();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
