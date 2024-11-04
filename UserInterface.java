/**
 * UserInterface Class
 *
 * User Class/Interface
 * - This class is used for storing each individual users' information.
 * This class stores things related to users such as username, userID, password,
 * profile picture, friends, and blocked users
 * The users class will interact heavily with the Database Manager class,
 * where it will store it along with the chat Data.
 *
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */

import java.util.ArrayList;

public interface UserInterface {
    // getters
    String getUsername();

    String getUserID();

    String getPassword();

    ArrayList<User> getFriends();

    ArrayList<User> getBlocked();

    ArrayList<String> getChatIDs();

    // setters
    void setPassword(String password);

    void setFriends(ArrayList<User> friends);

    void setBlocked(ArrayList<User> blocked);

    void setChatIDs(ArrayList<String> chatIDs);

    // other
    void addFriends(User user);

    void removeFriends(User user);

    void addBlocked(User user);

    void removeBlocked(User user);

    void setProfilePicturebase64(String profilePicturebase64);

    String getProfilePicturebase64();

    void clearProfilePicturebase64();

}
