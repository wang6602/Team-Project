import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String[] args) {


        // Test createUser method
        DatabaseManager myManager = new DatabaseManager();
        System.out.println("Testing createUser:");
        boolean userCreated1 = myManager.createUser("testUser", "password123");
        boolean userCreated2 = myManager.createUser("testUser2", "password1223");

    }
}
