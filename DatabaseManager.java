import java.util.ArrayList;
import java.io.*;

public class DatabaseManager implements DatabaseManagerInterface {
    public ArrayList<String> getChatIDs(User user) {
        try {
            File f = new File("chatIDs.txt");
            if (f.exists() == false) {
                f.createNewFile();
            }
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine;
            while(currentLine = bfr.readLine() != null) {
                String currentLine = bfr.readLine();
                if(currentLine.contains())
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
