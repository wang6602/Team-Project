public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.createUser("user1", "user1");
        db.createUser("user2", "user2");
        db.createUser("user3", "user3");
        db.addFriend("user1", "user2");
        db.addFriend("user1", "user3");

        //db.blockFriend("user1", "user2");
        String chat = db.createChat(new String[]{"user1", "user2", "user3"});
        String chat2 = db.createChat(new String[]{"user1", "user2"});
        String chat3 = db.createChat(new String[]{"user1", "user3"});
        db.newText("user1", chat, "hellow");

    }
}
