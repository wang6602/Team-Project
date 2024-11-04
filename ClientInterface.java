/**
 * ClientInterface Interface
 *
 * Client Class/Interface
 * This will be running on each individual user.
 * It will make a connection to the Client Handler class to connect to the network
 * This will be responsible for sending the information to Server, which will send
 * information to Database manager to execute desired actions
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */
public interface ClientInterface {

    public void login();

    public void logout();

    public void register();

    public void profile();

    public void createChat(String username);

    public void sendMessage(String username, String message);

    public void addFriend(String username);

    public void blockFriend(String username);

    public void viewChats();

    public void viewUsers(String userID);

    public void viewUsers();

}
