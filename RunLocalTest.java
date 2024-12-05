/**
 * RunLocalTest Class
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 * <p>
 * RunLocalTest
 * -Implements JUnit test cases that were written to ensure that methods in User class
 * and DatabaseManager class work as intended
 *
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


import java.util.ArrayList;

/**
 * Run local test case phase 1 Class
 * <p>
 * Runs all the test cases for phase 1.
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */

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
            Assert.assertTrue("The ArrayList should contain at least one element.", result.size() > 0);


        }

        @Test
        public void testUserinChat() {
            String result = dbManager.getUsersinChat(user2chat);
            Assert.assertTrue(result.equals("user2,user1,user3"));
        }


        @Test
        public void testnewText() {
            dbManager.newText("user1", user1chat, "hello");
            ArrayList<String> ans = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should contain at least one element.", ans.size() == 1);
            Assert.assertTrue(ans.get(0).equals("user1,hello"));
        }


        @Test
        public void testdeleteText() {
            dbManager.deleteText(user1chat, 0);
            ArrayList<String> ans = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should be empty.", ans.size() == 0);
        }


        @Test
        public void testcreateChat() {
            dbManager.createUser("user5", "user5");
            String newChat = dbManager.createChat(new String[]{"user1", "user2", "user5"});
            Assert.assertTrue("The ArrayList should have a chatID.", newChat != null);
        }


        @Test
        public void testcreateuser() {
            boolean result = dbManager.createUser("tstingcreateUser", "testingCreateUser");
            Assert.assertTrue("The ArrayList should have a userID.", result);


        }

        @Test
        public void testRemoveUser() {
            boolean result = dbManager.removeUser("tstingcreateUser");
            Assert.assertTrue("The ArrayList should have a userID.", result);
        }


        @Test
        public void testremoveuserfromchat() {
            dbManager.createUser("temporrary", "temporrary");
            dbManager.addUserToChat("temporrary", user1chat);
            boolean result = dbManager.removeUserFromChat("temporrary", user1chat);
            Assert.assertTrue("The ArrayList should remove this userID.", result);
        }


        @Test
        public void testreadChat() {
            dbManager.newText("user1", user1chat, "testing read chat");
            ArrayList<String> result = dbManager.readChat(user1chat);
            Assert.assertTrue("The ArrayList should contain at least one element.", result.size() > 0);
        }


        @Test
        public void testUpdateUser() {
            User u = new User("user1", "updatedUser1password");
            dbManager.updateUser(u);
            ArrayList<User> users = dbManager.userLookup("user1");
            Assert.assertTrue(users.get(0).getPassword().equals(u.getPassword()));
        }


        @Test
        public void testLoginUser() {
            boolean result = dbManager.loginUser("user3", "user3", "user3");
            Assert.assertTrue(result);
        }


        @Test
        public void testupdateprofilepic() {
            boolean result = dbManager.updateUserProfilePicture("user1", "New profile picture");
            Assert.assertTrue(result);
        }

        @Test
        public void testgetUserProfilePicture() {
            String result = dbManager.getUserProfilePicture("user1");
            Assert.assertTrue(result != null);
        }

        @Test
        public void testclearprofilepic() {
            boolean result = dbManager.clearUserProfilePicture("user1");
            Assert.assertTrue(result);
        }


        @Test
        public void testaddfriend() {
            dbManager.createUser("friend1", "friend1");
            dbManager.createUser("friend2", "friend2");
            boolean result = dbManager.addFriend("friend1", "friend2");
            Assert.assertTrue(result);
        }


        @Test
        public void testblockfriend() {
            dbManager.createUser("friendblock1", "friend1");
            dbManager.createUser("friendblock2", "friend2");
            boolean result = dbManager.blockFriend("friendblock1", "friendblock2");
            Assert.assertTrue(result);
        }


        @Test
        public void testuserviewer() {
            ArrayList<String> users = dbManager.userViewer();
            Assert.assertTrue(users.size() > 0);
        }

        @Test
        public void testuserlookup() {
            ArrayList<User> users = dbManager.userLookup("user1");
            Assert.assertTrue(users.get(0).getUsername().equals("user1"));
        }

        //Test for UserInterface implementation
        @Test
        public void testUserInterfaceImplementation() {
            User obj = new User("username", "password");
            Assert.assertTrue(obj instanceof UserInterface);
        }

        //Test for DatabaseManagerInterface implementation
        @Test
        public void testDatabaseManagerInterfaceImplementation() {
            DatabaseManager obj = new DatabaseManager();
            Assert.assertTrue(obj instanceof DatabaseManagerInterface);
        }

        @Test
        public void testConstructorUsernamePassword() {
            User user = new User("user1", "password1");
            Assert.assertEquals("Username should be 'user1'", "user1", user.getUsername());
            Assert.assertEquals("Password should be 'password1'", "password1", user.getPassword());
            Assert.assertNull("Profile picture should be null", user.getProfilePicturebase64());
            Assert.assertTrue("Friends list should be empty", user.getFriends().isEmpty());
            Assert.assertTrue("Blocked list should be empty", user.getBlocked().isEmpty());
            Assert.assertTrue("Chat IDs list should be empty", user.getChatIDs().isEmpty());
        }


        // 2. constructor User(String username, String password, String profilePicturebase64)
        @Test
        public void testConstructorWithProfilePicture() {
            User user = new User("user1", "password1", "profilePicBase64");
            Assert.assertEquals("Username should be 'user1'", "user1", user.getUsername());
            Assert.assertEquals("Password should be 'password1'", "password1", user.getPassword());
            Assert.assertEquals("Profile picture should be 'profilePicBase64'", "profilePicBase64", user.getProfilePicturebase64());
            Assert.assertTrue("Friends list should be empty", user.getFriends().isEmpty());
            Assert.assertTrue("Blocked list should be empty", user.getBlocked().isEmpty());
            Assert.assertTrue("Chat IDs list should be empty", user.getChatIDs().isEmpty());
        }


        // 3.  getUsername()
        @Test
        public void testGetUsername() {
            User user = new User("user1", "password1");
            Assert.assertEquals("Username should be 'user1'", "user1", user.getUsername());
        }


        // 4. getUserID()
        @Test
        public void testGetUserID() {
            User user = new User("user1", "password1");
            Assert.assertEquals("UserID should be 'user1'", "user1", user.getUserID());
        }


        // 5. ests getPassword()
        @Test
        public void testGetPassword() {
            User user = new User("user1", "password1");
            Assert.assertEquals("Password should be 'password1'", "password1", user.getPassword());
        }


        // 6. tests setPassword(String password)
        @Test
        public void testSetPassword() {
            User user = new User("user1", "oldPassword");
            user.setPassword("newPassword");
            Assert.assertEquals("Password should be updated to 'newPassword'", "newPassword", user.getPassword());
        }


        // 7. getFriends() initially
        //    friends list is initially empty.
        @Test
        public void testGetFriendsInitiallyEmpty() {
            User user = new User("user1", "password1");
            Assert.assertTrue("Friends list should be empty initially", user.getFriends().isEmpty());
        }


        // 8. addFriends(User user)
        //    friend is correctly added to the friends list.
        @Test
        public void testAddFriend() {
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");
            user1.addFriends(user2);
            Assert.assertTrue("User2 should be in user1's friends list", user1.getFriends().contains(user2));
        }


        // 9. removeFriends(User user)
        //   friend is correctly removed from the friends list.
        @Test
        public void testRemoveFriend() {
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");
            user1.addFriends(user2);
            user1.removeFriends(user2);
            Assert.assertFalse("User2 should not be in user1's friends list after removal", user1.getFriends().contains(user2));
        }


        // 10. setFriends(ArrayList<User> friends)
        //     friends list is correctly set.
        @Test
        public void testSetAndGetFriends() {
            User user = new User("user1", "password1");
            ArrayList<User> friends = new ArrayList<>();
            friends.add(new User("user2", "password2"));
            user.setFriends(friends);
            Assert.assertEquals("Friends list should be set to the given list", friends, user.getFriends());
        }


        // 11. getBlocked() initially
        //     the blocked list is initially empty.
        @Test
        public void testGetBlockedInitiallyEmpty() {
            User user = new User("user1", "password1");
            Assert.assertTrue("Blocked list should be empty initially", user.getBlocked().isEmpty());
        }


        // 12. addBlocked(User user)
        @Test
        public void testAddBlocked() {
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");
            user1.addBlocked(user2);
            Assert.assertTrue("User2 should be in user1's blocked list", user1.getBlocked().contains(user2));
        }


        // 13. removeBlocked(User user)
        @Test
        public void testRemoveBlocked() {
            User user1 = new User("user1", "password1");
            User user2 = new User("user2", "password2");
            user1.addBlocked(user2);
            user1.removeBlocked(user2);
            Assert.assertFalse("User2 should not be in user1's blocked list after removal", user1.getBlocked().contains(user2));
        }


        // 14. setBlocked(ArrayList<User> blocked)
        @Test
        public void testSetAndGetBlocked() {
            User user = new User("user1", "password1");
            ArrayList<User> blocked = new ArrayList<>();
            blocked.add(new User("user2", "password2"));
            user.setBlocked(blocked);
            Assert.assertEquals("Blocked list should be set to the given list", blocked, user.getBlocked());
        }


        // 15. getChatIDs() initially
        @Test
        public void testGetChatIDsInitiallyEmpty() {
            User user = new User("user1", "password1");
            Assert.assertTrue("Chat IDs list should be empty initially", user.getChatIDs().isEmpty());
        }


        // 16. setChatIDs(ArrayList<String> chatIDs)
        @Test
        public void testSetAndGetChatIDs() {
            User user = new User("user1", "password1");
            ArrayList<String> chatIDs = new ArrayList<>();
            chatIDs.add("chat1");
            chatIDs.add("chat2");
            user.setChatIDs(chatIDs);
            Assert.assertEquals("Chat IDs should be set to the given list", chatIDs, user.getChatIDs());
        }


        // 17. setProfilePicturebase64(String profilePicturebase64)
        @Test
        public void testSetAndGetProfilePicturebase64() {
            User user = new User("user1", "password1");
            user.setProfilePicturebase64("testProfilePic");
            Assert.assertEquals("Profile picture should be 'testProfilePic'", "testProfilePic", user.getProfilePicturebase64());
        }


        // 18. clearProfilePicturebase64()
        @Test
        public void testClearProfilePicturebase64() {
            User user = new User("user1", "password1", "initialProfilePic");
            user.clearProfilePicturebase64();
            Assert.assertNull("Profile picture should be null after clearing", user.getProfilePicturebase64());
        }


        // 19. adds chat IDs
        //     makes sure that the chat IDs can be added to the user's list.
        @Test
        public void testAddChatID() {
            User user = new User("user1", "password1");
            user.getChatIDs().add("chat123");
            Assert.assertTrue("Chat IDs list should contain 'chat123'", user.getChatIDs().contains("chat123"));
        }


        // 20. setting a new friends list
        @Test
        public void testSetNewFriendsList() {
            User user = new User("user1", "password1");
            ArrayList<User> oldFriends = new ArrayList<>();
            oldFriends.add(new User("user2", "password2"));
            user.setFriends(oldFriends);


            ArrayList<User> newFriends = new ArrayList<>();
            newFriends.add(new User("user3", "password3"));
            user.setFriends(newFriends);


            Assert.assertFalse("Old friends should not be in the friends list", user.getFriends().contains(oldFriends.get(0)));
            Assert.assertTrue("New friends should be in the friends list", user.getFriends().contains(newFriends.get(0)));
        }
    }
}
