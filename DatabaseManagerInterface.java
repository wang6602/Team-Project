
import java.util.ArrayList;

/**
 * DatabaseManagerInterface Class
 * <p>
 * - This class is used for interacting with the various databases, like messaging and the
 * users database
 * It will store the users in usersDatabase and chats in their own text files.
 * It also has a ChatID database to store the filenames of chats between certain users
 * This class will be responsible from receiving information from the server class
 * and will make new messages, delete texts, add users, delete users, and more
 *
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */

public interface DatabaseManagerInterface {
    // Chat methods
    ArrayList<String> getChatIDs(String userID);

    String getUsersinChat(String chatID);

    ArrayList<String> readChat(String chatID);

    void deleteText(String chatID, int index);

    String createChat(String[] userID);

    boolean newText(String currentUserID, String chatID, String message);

    // User methods
    boolean createUser(String username, String password);

    boolean removeUser(String userID);

    boolean addUserToChat(String chatID, String userID);

    boolean removeUserFromChat(String chatID, String userID);

    ArrayList<User> userLookup(String username);

    ArrayList<String> userViewer();

    void updateUser(User user);

    boolean loginUser(String username, String password, String userID);

    boolean updateUserProfilePicture(String userID, String base64);

    String getUserProfilePicture(String userID);

    boolean clearUserProfilePicture(String userID);

    boolean addFriend(String currentuserID, String friendID);

    boolean blockFriend(String currentuserID, String friendID);

}
