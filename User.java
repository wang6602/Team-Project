

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User Class
 * <p>
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
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */


public class User implements Serializable, UserInterface {
    private String username;
    private String userID;
    private String password;
    private String profilePicturebase64;
    private ArrayList<User> friends;
    private ArrayList<User> blocked;
    private ArrayList<String> chatIDs;

    public User(String username, String password) {
        this.username = username;
        this.userID = username;
        this.password = password;
        profilePicturebase64 = null;
        friends = new ArrayList<>();
        blocked = new ArrayList<>();
        chatIDs = new ArrayList<>();
    }

    public User(String username, String password, String profilePicturebase64) {
        this.username = username;
        this.userID = username;
        this.password = password;
        this.profilePicturebase64 = profilePicturebase64;
        friends = new ArrayList<>();
        blocked = new ArrayList<>();
        chatIDs = new ArrayList<>();
    }

    // all get methods
    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<User> getBlocked() {
        return blocked;
    }

    public ArrayList<String> getChatIDs() {
        return chatIDs;
    }

    // all set methods
    public void setPassword(String password) {
        this.password = password;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    public void setChatIDs(ArrayList<String> chatIDs) {
        this.chatIDs = chatIDs;
    }

    // adds specified user to friend ArrayList
    public void addFriends(User user) {
        friends.add(user);
    }

    // removes the specified user from friend ArrayList
    public void removeFriends(User user) {
        friends.remove(user);
    }

    // adds specified user to the blocked ArrayList
    public void addBlocked(User user) {
        blocked.add(user);
    }

    // removes specified user from the blocked ArrayList
    public void removeBlocked(User user) {
        blocked.remove(user);
    }

    // sets a new profile picture in base 64 format
    public void setProfilePicturebase64(String profilePicturebase64) {
        this.profilePicturebase64 = profilePicturebase64;
    }

    // gets the String formatted base 64 picture
    public String getProfilePicturebase64() {
        return profilePicturebase64;
    }

    // clears the base 64 pciture and sets it back to the defualt
    public void clearProfilePicturebase64() {
        profilePicturebase64 = null;
    }

}
