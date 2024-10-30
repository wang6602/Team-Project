import java.util.ArrayList;
import java.io.*;

public class DatabaseManager implements DatabaseManagerInterface {
    public ArrayList<String> getChatIDs() {
        try {
            FileOutputStream fos = new FileOutputStream("chatIDs.txt");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
