import java.util.ArrayList;

public interface DatabaseManagerInterface {
    public ArrayList<String> getChatID();
    public ArrayList<String> readChat();
    public ArrayList<String> readEndChat();
    public void newText();
    public void deleteText();
    public void createChat();
    public boolean createUser();
    public boolean removeUser();
    public boolean removeUserFromChat();
    public boolean addUserToChat();
    public ArrayList<> userLookup();
    public ArrayList<> userViewer();
    public void updateUser();
    public boolean loginUser();


}
