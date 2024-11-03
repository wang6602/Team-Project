import java.util.ArrayList;

public interface DatabaseManagerInterface {
    // Chat methods
    ArrayList<String> getChatIDs(String userID);

    String getUsersinChat(String chatID);

    ArrayList<String> readChat(String chatID);

    void deleteText(String chatID, int index);

    void createChat(String[] userID);

    void newText(String currentUserID, String chatID, String message);

    // User methods
    boolean createUser(String username, String password);

    boolean removeUser(String userID);

    boolean addUserToChat(String chatID, String userID);

    boolean removeUserFromChat(String chatID, String userID);

    ArrayList<User> userLookup(String username);

    ArrayList<User> userViewer();

    void updateUser(User user);

    boolean loginUser(String username, String password, String userID);

    boolean updateUserProfilePicture(String userID, String base64);

    String getUserProfilePicture(String userID);

    boolean clearUserProfilePicture(String userID);

    boolean addFriend(String currentuserID, String friendID);

    boolean blockFriend(String currentuserID, String friendID);

}
