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

        @Before
        public void setUp() {
            dbManager = new DatabaseManager();
            // Sample setup for test data if needed
        }

        // Test for adding a user to a chat
        @Test
        public void testAddUserToChat() {
            boolean result = dbManager.addUserToChat("user1", "chat1");
            Assert.assertTrue("User should be added to the chat", result);
        }

        // Test for removing a user from a chat
        @Test
        public void testRemoveUserFromChat() {
            dbManager.addUserToChat("user2", "chat2"); // Pre-condition
            boolean result = dbManager.removeUserFromChat("user2", "chat2");
            Assert.assertTrue("User should be removed from the chat", result);
        }

        // Test for successful login
        @Test
        public void testLoginUserSuccess() {
            dbManager.createUser("testUser", "password"); // Simulate user creation
            boolean result = dbManager.loginUser("testUser", "password", "testUserID");
            Assert.assertTrue("Login should succeed with valid credentials", result);
        }

        // Test for failed login with invalid credentials
        @Test
        public void testLoginUserFail() {
            boolean result = dbManager.loginUser("invalidUser", "wrongPassword", "userID");
            Assert.assertFalse("Login should fail with incorrect credentials", result);
        }

        // Test for adding a friend
        @Test
        public void testAddFriend() {
            dbManager.createUser("user1", "pass1");
            dbManager.createUser("user2", "pass2");
            dbManager.addFriend("user1", "user2");
            User user1 = dbManager.userLookup("user1").get(0);
            Assert.assertTrue("User2 should be in User1's friend list", user1.getFriends().contains("user2"));
        }

        // Test for blocking a friend
        @Test
        public void testBlockFriend() {
            dbManager.createUser("user1", "pass1");
            dbManager.createUser("user2", "pass2");
            dbManager.blockFriend("user1", "user2");
            User user1 = dbManager.userLookup("user1").get(0);
            Assert.assertTrue("User2 should be in User1's blocked list", user1.getBlocked().contains("user2"));
        }

        // Test for sending a message
        @Test
        public void testSendMessage() {
            dbManager.createChat( new String[]{"user1", "user2"});
            dbManager.newText("user1", "Hello", "chat1");
            ArrayList<String> chatMessages = dbManager.readChat("chat1");
            Assert.assertTrue("Chat should contain the sent message", chatMessages.contains("Hello"));
        }

        // Test for viewing chat messages
        @Test
        public void testViewChat() {
            dbManager.createChat(new String[]{"user1", "user2"});
            dbManager.newText("user1", "chat1", "Hello");
            ArrayList<String> chatMessages = dbManager.readChat("chat1");
            Assert.assertNotNull("Messages should not be null", chatMessages);
            Assert.assertTrue("Messages should contain expected text", chatMessages.contains("Hello"));
        }

        // Test for viewing a user profile
        @Test
        public void testViewUser() {
            dbManager.createUser("user1", "pass1");
            User user = dbManager.userViewer().get(0);
            Assert.assertNotNull("User should not be null", user);
            Assert.assertEquals("User ID should match", "user1", user.getUserID());
        }

        // Test for adding text to a chat
        @Test
        public void testAddText() {
            dbManager.createChat(new String[]{"user1", "user2"});
            dbManager.newText("user1","chat1", "Hello, world!");
            ArrayList<String> chatMessages = dbManager.readChat("chat1");
            Assert.assertTrue("Message should be added to chat", chatMessages.contains("Hello, world!"));
        }

        // Test for deleting text from a chat
        @Test
        public void testDeleteText() {
            dbManager.createChat(new String[]{"user1", "user2"});
            dbManager.newText("user1", "chat1", "Temporary message");
            dbManager.deleteText("chat1", 0); // Assuming 0 is the message index
            ArrayList<String> chatMessages = dbManager.readChat("chat1");
            Assert.assertFalse("Message should be deleted from chat", chatMessages.contains("Temporary message"));
        }

        // Test for removing a user from the database
        @Test
        public void testRemoveUser() {
            dbManager.createUser("testUser", "password");
            boolean result = dbManager.removeUser("testUser");
            Assert.assertTrue("The user should be removed from the database", result);
        }

        // Test for viewing all users
        @Test
        public void testUserViewer() {
            dbManager.createUser("user1", "pass1");
            ArrayList<User> users = dbManager.userViewer();
            Assert.assertNotNull("User list should not be null", users);
            Assert.assertTrue("User list should contain at least one user", users.size() > 0);
        }

        // Test for looking up a specific user
        @Test
        public void testUserLookup() {
            dbManager.createUser("lookupUser", "password");
            ArrayList<User> result = dbManager.userLookup("lookupUser");
            Assert.assertFalse("User lookup should return at least one match", result.isEmpty());
        }

        // Test for updating user information
        @Test
        public void testUpdateUser() {
            User user = new User("updateUserID", "password");
            dbManager.updateUser(user);
            User updatedUser = dbManager.userLookup("updateUserID").get(0);
            Assert.assertEquals("Updated user should have correct ID", "updateUserID", updatedUser.getUserID());
        }

        // Test for reading chat messages for an existing chat
        @Test
        public void testReadChat() {
            ArrayList<String> chatIDs = new ArrayList<>();
            chatIDs.add("testChatID");
            ArrayList<String> messages = dbManager.readChat(chatIDs.get(0));
            Assert.assertNotNull("Messages should not be null", messages);
        }

        // Test for reading a non-existent chat
        @Test
        public void testReadChatNonExistent() {
            ArrayList<String> chatIDs = new ArrayList<>();
            chatIDs.add("nonExistentChatID");
            ArrayList<String> messages = dbManager.readChat(chatIDs.get(0));
            Assert.assertTrue("Messages list should be empty for a non-existent chat", messages.isEmpty());
        }
    }
}
