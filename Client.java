import java.io.*;
import java.net.*;

/*
This class runs for each individual Client.

It makes a connection to the Client Handler class using Server I/O after receiving the desired
operation from the user.

The processing itself is carried out in the ClientHandler class.
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

    public void setUserIDandUsername(String userID) {
        this.userID = userID;
        this.username = userID;
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

    public void newText(String chatID, String message) {
        try {
            out.println("POSTTEXT:" + userID + ":" + chatID + ":" + message);


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getUserProfilePicture(String userID) {
        try {
            out.println("GETUSERPROFILEPICTURE:" + userID);
            return (in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean clearUserProfilePicture(String userID) {
        try {
            out.println("CLEARUSERPROFILEPICTURE:" + userID);
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

    public String createChat(String[] userID) {
        try {
            String send = "";
            for (String temp : userID) {
                send += temp + ",";
            }
            out.println("CREATECHAT:" + send.substring(0, send.length() - 1));
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createUser(String username, String password) {
        try {
            out.println("CREATEUSER:" + username + ":" + password);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUser(String userID) {
        try {
            out.println("REMOVEUSER:" + userID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUserFromChat(String username, String chatID) {
        try {
            out.println("REMOVEUSERFROMCHAT:" + username + ":" + chatID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tatjana's methods tehe
    /*
    1. boolean addUsertochat(String username, String chatID)
    2. String[] userLookup(String name)
    3. String[] userViewer()
          - Returns a list of all the user’s username
    4. Void Updateuser(User user)
    5. boolean loginuser(String username, String password, String userID)

    */


    public boolean addUserToChat(String username, String chatID) {
        try {
            out.println("ADDUSERTOCHAT:" + username + ":" + chatID);
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

    public void updateUser(String username, String password) {
        try {
            out.println("UPDATEUSER:" + username + ":" + password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loginUser(String username, String password, String userID) {
        try {
            out.println("LOGINUSER:" + username + ":" + password + ":" + userID);
            return Boolean.parseBoolean(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.sendMessage();
    }

}

