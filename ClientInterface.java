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
