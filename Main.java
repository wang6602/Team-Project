public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        db.createUser("user1", "user1");
        db.createUser("user2", "user2");
        db.createUser("user3", "user3");
//        db.addFriend("user1", "user2");
//        db.addFriend("user1", "user3");
//
//        //db.blockFriend("user1", "user2");
//        String chat = db.createChat(new String[]{"user1", "user2", "user3"});
//        String chat2 = db.createChat(new String[]{"user1", "user2"});
//        String chat3 = db.createChat(new String[]{"user1", "user3"});
//        db.newText("user1", chat, "hellowjklh");
        Client user1 = new Client();
        user1.setUserIDandUsername("user1");
        System.out.println(user1.addFriend("user1", "user2"));
        System.out.println(user1.addFriend("user1", "user3"));
        String newchat = user1.createChat(new String[]{"user1", "user2"});
        String newchat2 = user1.createChat(new String[]{"user1", "user3"});
        String newchat3 = user1.createChat(new String[]{"user1", "user2", "user3"});



        System.out.println(user1.newText(newchat, "hello ther2"));
        System.out.println(user1.newText(newchat2, "hello ther3"));
        System.out.println(user1.newText(newchat3, "hello ther4"));




    }
}
