/**
 * ClientInterface Interface
 * <p>
 * Client Class/Interface
 * This will be running on each individual user.
 * It will make a connection to the Client Handler class to connect to the network
 * This will be responsible for sending the information to Server, which will send
 * information to Database manager to execute desired actions
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */
public interface ClientInterface {

    String[] getChatIDs();

    String getUsersInChat(String chatID);

    String[] readChat(String chatID);

    void newText(String chatID, String message);

    void deleteText(String chatID, int index);

    String createChat(String[] users);

    boolean createUser(String username, String password);

    boolean removeUser(String userID);

    boolean removeUserFromChat(String username, String chatID);

    boolean addUserToChat(String username, String chatID);

    String[] userLookup(String name);

    String[] userViewer();

    void updateUser(String username, String password);

    boolean loginUser(String username, String password, String userID);

}
