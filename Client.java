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
            System.out.println("Connected");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUserIDandUsername(String userID){
        this.userID = userID;
        this.username = userID;
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

    public String getUserProfilePicture(String userID) {
        try {
            out.println("GETUSERPROFILEPICTURE:" + userID);
            return(in.readLine());

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

    public  boolean addFriend(String currentuserID, String friendID) {
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


    public void deleteText(String chatID, String message) {
        try {
            out.println("CHATID:" + chatID + ":MSGCONTENT:" + message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createChat(String chatID, String message) {
        try {
            out.println("CREATECHAT:" + chatID + ":" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(String username, String password) {
        try {
            out.println("CREATEUSER:" + username + ":" + password);
            return (in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean removeUser(String userID) {
        try {
            out.println("REMOVEUSER:" + userID);
            return(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean removeUserFromChat(String username, String chatID) {
        try {
            out.println("REMOVEUSERFROMCHAT:" + username + ":" + chatID);
            return(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        Client client = new Client();
        client.sendMessage();
    }

}

