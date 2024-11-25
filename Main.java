public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.createUser("user1", "user1");
        db.createUser("user2", "user2");
        db.createUser("user3", "user3");

        Client user1 = new Client();
        user1.setUserIDandUsername("user1");
        String chat = user1.createChat(new String[]{"user1","user2", "user3"});
        String chat2 = user1.createChat(new String[]{"user1","user2"});
        user1.newText(chat, "hello");
        user1.newText(chat2, "world");




    }
}
