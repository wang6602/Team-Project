import java.util.ArrayList;

public interface UserInterface {
    // getters
    String getUserName();

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
    void addFriend(User user);

    void removeFriend(User user);

    void addBlocked(User user);

    void removeBlocked(User user);

}
