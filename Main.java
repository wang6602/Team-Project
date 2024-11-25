public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.createUser("user1", "user1");
        db.createUser("user2", "user2");
        db.createUser("user3", "user3");

        Client user1 = new Client();
        user1.setUserIDandUsername("user1");
        user1.newText("hello", "hi");




    }
}
