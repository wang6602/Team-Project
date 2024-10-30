import java.util.ArrayList;

public interface DatabaseManagerInterface {
    //Chat methods
    ArrayList<String> getChatIDs();
    ArrayList<String> readChat(String chatID);
    ArrayList<String> readEndChat(String chatID);
    void addText(String chatID, String message);
    void deleteText(String chatID, int messageID);
    void createChat(String chatName);

    //User methods
    boolean createUser(String username, String password);
    boolean removeUser(String userID);
    boolean addUserToChat(String chatID, String userID);
    boolean removeUserFromChat(String chatID, String userID);
    ArrayList<String> userLookup(String username);
    ArrayList<String> userViewer(String userID);
    void updateUser(String userID, String newInfo);
    boolean loginUser(String username, String password);
}
