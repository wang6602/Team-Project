import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String[] args) {


        // Test createUser method
        DatabaseManager myManager = new DatabaseManager();
        System.out.println("Testing createUser:");
        boolean userCreated1 = myManager.createUser("testUser", "password123");
        boolean userCreated2 = myManager.createUser("testUser2", "password1223");
        System.out.println(myManager.getChatIDs("testUser"));
        boolean userCreated3 = myManager.createUser("testUser3", "password123");
        boolean userCreated4 = myManager.createUser("testUser4", "password123");
        myManager.addUserToChat("testUser3", "430f6d74-6e5e-4090-940f-3eadb34f86ab");
        myManager.addUserToChat("testUser4", "430f6d74-6e5e-4090-940f-3eadb34f86ab");
        myManager.removeUser("testUser3");



    }
}
