
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Run local test case phase 2 Class
 * <p>
 * Runs all the test cases for phase 2. Make sure to be running the server first.
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */

public class RunLocalTestPhase2 {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(RunLocalTest.TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully.");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {
        DatabaseManager db = new DatabaseManager();


        Client client = new Client();
        String chat1;


        @Before
        public void setUp() {
            client.setUserIDandUsername("user1");
            db.createUser("user1", "user1");
            db.createUser("user2", "user2");
            db.createUser("user3", "user3");
            db.createUser("user4", "user4");

            db.createUser("usertestupdate", "usertestupdate");
            chat1 = db.createChat(new String[]{"user1", "user2"});
            db.updateUserProfilePicture("user1", "profilepicbase64");

            client.updateUser("usertestupdate", "testuserpassword");


        }

        @Test
        public void testClientgetChatID() {
            String[] ans = client.getChatIDs();
            Assert.assertTrue(ans[ans.length - 1].equals(chat1));
        }

        @Test
        public void testGetUserInChat() {
            String ans = client.getUsersInChat(chat1);
            Assert.assertTrue(ans.equals("user1,user2"));

        }

        @Test
        public void testNewTextandReadText() {
            client.newText(chat1, "hello user2 my name is user1");
            String[] ans = client.readChat(chat1);
            Assert.assertTrue(ans[1].equals("hello user2 my name is user1"));
        }

        @Test
        public void testAddFriend() {
            boolean ans = client.addFriend("user1", "user2");
            Assert.assertTrue(ans == true);
        }

        @Test
        public void testBlockFriend() {
            boolean ans = client.blockFriend("user1", "user2");
            Assert.assertTrue(ans == true);
        }

        @Test
        public void testGetUserProfilePicture() {
            String ans = client.getUserProfilePicture("user1");
            Assert.assertTrue(ans.equals("profilepicbase64"));
        }

        @Test
        public void testClearUserProfilePicture() {
            boolean ans = client.clearUserProfilePicture("user1");
            Assert.assertTrue(ans == true);
        }

        @Test
        public void deleteText() {
            db.newText("user1", chat1, "testing the delete text");
            client.deleteText(chat1, 1);
            String[] chat = client.readChat(chat1);
            Assert.assertTrue(chat.length == 2);

        }

        @Test
        public void createChat() {
            String newChat = client.createChat(new String[]{"user1", "user2"});
            ArrayList<String> s = db.getChatIDs("user1");
            //String ans = s.get(s.size()-1);
            Assert.assertTrue(s.contains(newChat));
        }

        @Test
        public void testCreateUser() {
            //MUST MODIFY USERNAME AFTER EVERY TEST CASE; ONLY WORKS ONCE PER USER
            boolean newUser = client.createUser("newUser1", "password123");
            Assert.assertTrue(newUser == true);
        }

        @Test
        public void testRemoveUser() {
            //Tests the removeUser method from Client.java.
            //Boolean will equal true if it is successful, passing the test case
            boolean removeUser = client.removeUser("user1");
            Assert.assertTrue(removeUser);
        }

        @Test
        public void testRemoveUserFromChat() {
            ArrayList<String> chatIDs = db.getChatIDs("user1");
            String firstChat = chatIDs.get(0);
            boolean removeUser = client.removeUserFromChat("user1", firstChat);
            Assert.assertTrue(removeUser);
        }

        @Test
        public void addUserToChat() {
            boolean ans = client.addUserToChat("user2", chat1);
            Assert.assertTrue(ans);
        }


        @Test
        public void testuserLookup() {
            String[] ans = client.userLookup("user2");
            Assert.assertTrue(ans[0].equals("user2"));
        }


        @Test
        public void testuserViewer() {
            String[] ans = client.userViewer();
            Assert.assertTrue(ans.length > 0);


        }


        @Test
        public void testUpdateUser() {


            try {
                FileInputStream fis = new FileInputStream(new File("userDatabase.txt"));
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                boolean userupdated = false;
                for (User user : users) {
                    if (user.getUserID().equals("usertestupdate") && user.getPassword().equals("testuserpassword")) {
                        userupdated = true;
                    }
                }

                Assert.assertTrue(userupdated == true);


            } catch (Exception e) {
                e.printStackTrace();

            }


        }

        @Test
        public void testLoginUser() {
            client.updateUser("user1", "testuserpasswordnew");
            boolean ans = client.loginUser("user1", "testuserpasswordnew", "user1");
            Assert.assertTrue(ans);

        }


    }


}
