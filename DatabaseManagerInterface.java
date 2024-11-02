import java.util.ArrayList;

public interface DatabaseManagerInterface {
    //Chat methods
    ArrayList<String> getChatIDs(String userID);
    ArrayList<String> readChat(String chatID);
    void deleteText(String currentUserID, String ID, int index);
    void createChat(String[] userID);
    void newText(String currentUserID, String chatID, String message);

    //User methods
    boolean createUser(String username, String password);
    boolean removeUser(String userID);
    boolean addUserToChat(String chatID, String userID);
    boolean removeUserFromChat(String chatID, String userID);
    ArrayList<User> userLookup(String username);
    ArrayList<String> userViewer(String userID);
    void updateUser(String userID, String newInfo);
    boolean loginUser(String username, String password);
}
