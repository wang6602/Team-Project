import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String[] args) {


        // Test createUser method
        DatabaseManager myManager = new DatabaseManager();
        System.out.println("Testing createUser:");
        boolean userCreated = myManager.createUser("testUser", "password123");
        System.out.println("User created: " + userCreated);
    }
}
