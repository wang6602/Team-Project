import java.util.ArrayList;
import java.io.*;

public class DatabaseManager implements DatabaseManagerInterface {
    public ArrayList<String> getChatIDs() {
        try {
            File f = new File("chatIDs.txt");
            if (f.exists() == false) {
                f.createNewFile();
            }
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine = bfr.readLine();
            while(currentLine != null) {

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
