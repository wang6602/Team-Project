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
