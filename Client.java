import java.io.*;
import java.net.*;




/**
 * <p>
 * Client Class
 * This class runs for each individual Client.
 * <p>
 * It makes a connection to the Client Handler class using Server I/O after receiving the desired
 * operation from the user.
 * <p>
 * The processing itself is carried out in the ClientHandler class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */

public class Client implements ClientInterface {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String username;
    private String password;
    private String userID;






    public Client() {
        try {
            socket = new Socket("localhost", 4242);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }



    public void setUserIDandUsername(String currentuserID) {
        this.userID = currentuserID;
        this.username = currentuserID;
    }

    public void sendMessage() {
        try {
            out.println("hello world");
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getChatIDs() {
        try {
            out.println("GETCHATID:" + userID);
            String[] result = in.readLine().split(",");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUsersInChat(String chatID) {
        try {
            out.println("GETUSERINCHAT:" + chatID);
            return (in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] readChat(String chatID) {
        try {
            out.println("READCHAT:" + chatID);
            String[] result = in.readLine().split(",");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean newText(String chatID, String message) {
        try {
            out.println("POSTTEXT:" + userID + ":" + chatID + ":" + message);
            boolean input = Boolean.parseBoolean(in.readLine());
            return input;


        } catch (Exception e) {

            e.printStackTrace();
            System.out.print("someerror coccured");
            return true;
        }
    }

    public String getUserProfilePicture(String currentuserID) {
        try {
            out.println("GETUSERPROFILEPICTURE:" + currentuserID);
            return (in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean clearUserProfilePicture(String currentuserID) {
        try {
            out.println("CLEARUSERPROFILEPICTURE:" + currentuserID);
            return Boolean.parseBoolean((in.readLine()));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addFriend(String currentuserID, String friendID) {
        try {
            out.println("ADDFRIEND:" + currentuserID + ":" + friendID);
            return Boolean.parseBoolean((in.readLine()));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean blockFriend(String currentuserID, String friendID) {
        try {
            out.println("BLOCKFRIEND:" + currentuserID + ":" + friendID);
            return Boolean.parseBoolean((in.readLine()));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public void deleteText(String chatID, int index) {
        try {
            out.println("DELETETEXT:" + chatID + ":" + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createChat(String[] userIDList) {
        try {
            String send = "";
            for (String temp : userIDList) {
                send += temp + ",";
            }
            out.println("CREATECHAT:" + send.substring(0, send.length() - 1));
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createUser(String newusername, String newpassword) {
        try {
            out.println("CREATEUSER:" + newusername + ":" + newpassword);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUser(String specifiedUserID) {
        try {
            out.println("REMOVEUSER:" + specifiedUserID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUserFromChat(String specifiedUsername, String chatID) {
        try {
            out.println("REMOVEUSERFROMCHAT:" + specifiedUsername + ":" + chatID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUserToChat(String newusername, String chatID) {
        try {
            out.println("ADDUSERTOCHAT:" + newusername + ":" + chatID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] userLookup(String name) {
        try {
            out.println("USERLOOKUP:" + name);
            return in.readLine().split(",");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] userViewer() {
        try {
            out.println("USERVIEWER:");
            return (in.readLine().split(","));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(String newusername, String newpassword) {
        try {
            out.println("UPDATEUSER:" + newusername + ":" + newpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loginUser(String currentusername, String clientPassword, String clientuserID) {
        try {
            out.println("LOGINUSER:" + currentusername + ":" + clientPassword + ":" + clientuserID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        Client client = new Client();

    }

}

