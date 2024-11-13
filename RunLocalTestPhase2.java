
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


import java.util.ArrayList;

//make sure to clear the chatID file and delete the userDatabase.txt before running


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
            chat1 = db.createChat(new String[]{"user1", "user2"});

        }
        @Test
        public void testClientgetChatID() {
            String[] ans = client.getChatIDs();
            Assert.assertTrue(ans[ans.length-1].equals(chat1));
        }

        @Test
        public void testGetUserInChat() {
            String ans = client.getUsersInChat(chat1);
            Assert.assertTrue(ans.equals("user1,user2"));

        }

        @Test
        public void testNewTextandReadText(){
            client.newText(chat1, "hello user2 my name is user1");
            String[] ans = client.readChat(chat1);
            Assert.assertTrue(ans[1].equals("hello user2 my name is user1"));
        }



    }


}