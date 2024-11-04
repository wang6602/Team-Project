import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;

public class RunLocalTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCase.class);
        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully.");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    public static class TestCase {

        private DatabaseManager dbManager;
        String user1chat;
        String user2chat;

        @Before
        public void setUp() {
            dbManager = new DatabaseManager();
            dbManager.createUser("user1", "user1");
            dbManager.createUser("user2", "user2");
            dbManager.createUser("user3", "user3");
            user1chat = dbManager.createChat(new String[]{"user1", "user2"});
            user2chat = dbManager.createChat(new String[]{"user2", "user1", "user3"});
            // Sample setup for test data if needed
        }

        // Test for adding a user to a chat
        @Test
        public void testAddUserToChat() {
            boolean result = dbManager.addUserToChat("user3", user1chat);
            Assert.assertTrue("User should be added to the chat", result);
        }

        @Test
        public void testRemoveUserFromChat() {
            boolean result = dbManager.removeUserFromChat("user3", user2chat);
            Assert.assertTrue("User should be removed from the chat", result);
        }

        @Test
        public void testgetChatIDs() {
            ArrayList<String> result = dbManager.getChatIDs("user1");
            Assert.assertTrue( "The ArrayList should contain at least one element.", result.size() > 0);

        }
        @Test
        public void testUserinChat(){
            String result = dbManager.getUsersinChat(user2chat);
            Assert.assertTrue( result.equals("user2,user1,user3"));
        }

        @Test
        public void testnewText(){
            dbManager.newText("user1", user1chat, "hello");
            ArrayList<String> ans = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should contain at least one element.", ans.size() == 1);
            Assert.assertTrue(ans.get(0).equals("user1,hello"));
        }

        @Test
        public void testdeleteText(){
            dbManager.deleteText(user1chat, 0);
            ArrayList<String> ans = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should be empty.", ans.size() == 0);
        }

        @Test
        public void testcreateChat(){
            dbManager.createUser("user5", "user5");
            String newChat = dbManager.createChat(new String[]{"user1", "user2", "user5" });
            Assert.assertTrue("The ArrayList should have a chatID.", newChat!=null);
        }

        @Test
        public void testcreateuser(){
            Boolean result = dbManager.createUser("tstingcreateUser", "testingCreateUser");
            Assert.assertTrue("The ArrayList should have a userID.", result);

        }
        @Test
        public void testRemoveUser(){
            Boolean result = dbManager.removeUser("tstingcreateUser");
            Assert.assertTrue("The ArrayList should have a userID.", result);
        }

        @Test public void testremoveuserfromchat(){
            dbManager.createUser("temporrary", "temporrary");
            dbManager.addUserToChat("temporrary", user1chat);
            Boolean result = dbManager.removeUserFromChat("temporrary", user1chat);
            Assert.assertTrue("The ArrayList should remove this userID.", result);
        }

        @Test
        public void testreadChat(){
            dbManager.newText("user1", user1chat, "testing read chat");
            ArrayList<String> result = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should contain at least one element.", result.size() > 0);
        }

        @Test
        public void testUpdateUser(){
            User u = new User("user1", "updatedUser1password");
            dbManager.updateUser(u);
            ArrayList<User> users = dbManager.userLookup("user1");
            Assert.assertTrue(users.get(0).getPassword().equals(u.getPassword()));
        }

        @Test
        public void testLoginUser(){
            Boolean result = dbManager.loginUser("user3", "user3", "user3");
            Assert.assertTrue(result);
        }

        @Test
        public void testupdateprofilepic(){
            Boolean result = dbManager.updateUserProfilePicture("user1", "New profile picture");
            Assert.assertTrue(result);
        }
        @Test
        public void testgetUserProfilePicture(){
            String result = dbManager.getUserProfilePicture("user1");
            Assert.assertTrue(result!=null);
        }
        @Test
        public void testclearprofilepic(){
            Boolean result = dbManager.clearUserProfilePicture("user1");
            Assert.assertTrue(result);
        }

        @Test
        public void testaddfriend(){
            dbManager.createUser("friend1", "friend1");
            dbManager.createUser("friend2", "friend2");
            Boolean result = dbManager.addFriend("friend1", "friend2");
            Assert.assertTrue(result);
        }

        @Test
        public void testblockfriend(){
            Boolean result = dbManager.blockFriend("friend1", "friend2");
            Assert.assertTrue(result);
        }

        @Test public void testuserviewer(){
            ArrayList<User> users = dbManager.userViewer();
            Assert.assertTrue(users.size()>0);
        }
        @Test public void testuserlookup(){
            ArrayList<User> users = dbManager.userLookup("user1");
            Assert.assertTrue(users.get(0).getUsername().equals("user1"));
        }








    }
}
