public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.createUser("user4", "user4");
        Client user1 = new Client();
        user1.setUserIDandUsername("user1");
        System.out.println(user1.addFriend("user1", "user4"));


    }
}
