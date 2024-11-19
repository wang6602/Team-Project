import java.io.*;
import java.util.*;

/**
 * DatabaseManager Class
 * <p>
 * DatabaseManager Class
 * - This class is used for interacting with the various databases, like messaging and the users database
 * It will store the users in usersDatabase and chats in their own text files.
 * It also has a ChatID database to store the filenames of chats between certain users
 * This class will be responsible from receiving information from the server class to create new messages
 * delete texts, add users, delete users, and more
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @version November 3, 2024
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 */

//  is only temporary
public class DatabaseManager implements DatabaseManagerInterface {

    public static Object gatekeeper = new Object();

    public ArrayList<String> getChatIDs(String userID) {
        /*
         * User the userID of the user to return the chat ID’s that the given user is
         * in.
         * This method will look through the ChatID Database to retrieve the ChatID’s
         * that
         * the user is in.
         */
        ArrayList<String> chatIDs = new ArrayList<>();

        BufferedReader bfr = null;
        try {
            synchronized (gatekeeper) {
                File f = new File("chatIDs.txt");
                if (f.exists() == false) {
                    f.createNewFile();
                }
                FileReader fr = new FileReader(f);
                bfr = new BufferedReader(fr);
                String currentLine;
                while ((currentLine = bfr.readLine()) != null) {
                    if (currentLine.contains(userID)) {
                        chatIDs.add(currentLine.split(",")[0]);
                    }
                }
                bfr.close();
            }
            return chatIDs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bfr != null) {
                    bfr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUsersinChat(String chatID) {
        /*
         * Returns a comma separated value of the various usernames in given chatID
         * using the ChatID database file. Useful for the current user to know who he
         * is sending a message to.
         */
        ArrayList<String> userIDs = new ArrayList<>();
        File file = new File("chatIDs.txt");
        BufferedReader bfr = null;
        synchronized (gatekeeper) {

            try {

                if (file.exists() == false) {
                    file.createNewFile(); // creates new file if file doesn't exist
                }
                FileReader fr = new FileReader(file);
                bfr = new BufferedReader(fr);
                String currentLine = bfr.readLine();
                while ((currentLine) != null) {
                    if (currentLine.contains(chatID)) {
                        return currentLine.substring(currentLine.indexOf(",") + 1);
                    }
                    currentLine = bfr.readLine();
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bfr != null) bfr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ArrayList<String> readChat(String chatID) {
        /*
         * Makes a connection to the text file of the given chat. Loops through each
         * line,
         * storing each chat line into a String arrayList. Ex
         * [“UserID1A,Hello”,”UserID2,Hello”...
         */
        ArrayList<String> texts = new ArrayList<>();
        BufferedReader bfr = null;
        try {
            synchronized (gatekeeper) {
                File f = new File(chatID + ".txt");
                if (f.exists() == false) {
                    f.createNewFile();
                }
                FileReader fr = new FileReader(f);
                bfr = new BufferedReader(fr);

                String line;
                while ((line = bfr.readLine()) != null) {
                    texts.add(line);
                }
                bfr.close();
            }

            return texts;
        } catch (Exception e) {
            e.printStackTrace();
            return texts;
        } finally {
            try {
                if (bfr != null) bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void newText(String currentUserID, String chatID, String message) {
        /*
         * Makes a connection to the given chatID, then adds a new line to the database
         * of UserID,message
         * Ensure that this method properly locks the file when writing to avoid
         * concurrency
         * issues if multiple users
         * are sending messages simultaneously, likely using synchronized(obj)
         */
        PrintWriter pw = null;
        try {
            synchronized (gatekeeper) {
                File f = new File(chatID + ".txt");
                if (f.exists() == false) {
                    f.createNewFile();
                }

                //NEW CODE STARTS HERE TO CHECK IF CURRENT USER HAS ADDED THEM AS FRIENDS

                boolean areFriends = true;

                String[] peopleInChat = new String[0];
                BufferedReader bfr = new BufferedReader(new FileReader("chatIDs.txt"));
                String currentLine = bfr.readLine();
                while ((currentLine) != null) {
                    if (currentLine.contains(chatID)) {
                        peopleInChat = currentLine.split(",");
                    }
                }

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("userDatabase.txt"));
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                for(User user : users) {
                    if(user.getUserID().equals(currentUserID)) {
                        ArrayList<User> currentUserFriends = user.getFriends();
                        for(String person : peopleInChat){
                            boolean personIsFriend = false;
                            for(User userFriend : currentUserFriends) {
                                if(userFriend.getUserID().equals(person)) {
                                    personIsFriend = true;
                                }
                            }
                            if(!personIsFriend) {
                                areFriends = false;
                                //return false;
                            }

                        }
                    }
                }


                //NEW CODE HERE FOR IF OTHER FREINDS BLOCKED CURRENT USER

                boolean isBlocked = false;
                for(int i =1; i< peopleInChat.length; i++){
                    for(int j = 0; j < users.size(); j++){
                        if(peopleInChat[i].equals(users.get(j).getUserID())) {
                            ArrayList<User> userBlocked = users.get(j).getBlocked();
                            for(User blocked: userBlocked) {
                                if(blocked.getUserID().equals(currentUserID)) {
                                    isBlocked = true;
                                }
                            }
                        }
                    }
                }

                /*

                if(!(areFreinds && !isBlocked)){
                    return false;
                }
                 */

                FileOutputStream fos = new FileOutputStream(f, true); // Append mode
                pw = new PrintWriter(fos);
                pw.println(currentUserID + "," + message);
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null)
                pw.close();
        }

    }

    public void deleteText(String chatID, int index) {
        /*
         * Make a connection with the file. Runs the readChat to get an
         * arrayList<String> of
         * all the messages. Using the passed in index, it deletes the according line
         * in the database. Then rewrites the whole database with ArrayList values
         */
        ArrayList<String> texts = new ArrayList<>();
        BufferedReader bfr = null;
        PrintWriter pw = null;
        try {
            synchronized (gatekeeper) {
                File f = new File(chatID + ".txt");
                if (f.exists()) {
                    FileReader fr = new FileReader(f);
                    bfr = new BufferedReader(fr);
                    String line;
                    while ((line = bfr.readLine()) != null) {
                        texts.add(line);
                    }
                    if (index < 0 || index >= texts.size()) {
                        return; // invalid index to delete method
                    }
                    texts.remove(index);
                    bfr.close();
                }

                synchronized (gatekeeper) {

                    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
                    pw = new PrintWriter(writer);
                    for (String text : texts) {
                        pw.println(text);
                    }
                    pw.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfr != null) bfr.close();
                if (pw != null) pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String createChat(String[] userID) {
        // Create a new entry in the ChatID database.
        // Assign a unique chatID for the group, then follow it with all the user IDs
        // involved
        // (ChatID, UserID1, UserID2, UserID3...).
        // Create a new text file with the ChatID representing the actual chat.
        PrintWriter pw = null;

        try {
            synchronized (gatekeeper) {
                File f = new File("chatIDs.txt");
                if (f.exists() == false) {
                    f.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(f, true);
                pw = new PrintWriter(fos);
                String chatID = UUID.randomUUID().toString();
                String usersInChat = "";
                for (int i = 0; i < userID.length; i++) {
                    usersInChat += userID[i] + ",";
                }

                pw.println(chatID + "," +
                        usersInChat.substring(0, usersInChat.length() - 1)); // prints the chatID to the
                // .txt file

                File newFile = new File(chatID + ".txt");

                pw.close();
                return chatID;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (pw != null) pw.close();
        }
    }

    public boolean createUser(String username, String password) {
        /*
         * Creates a new user with given username and password and UserID
         * Creates a temporary Users arrayList. Makes a connection to the
         * userDatabase file and retrieves that arrayList of Users from that text file.
         * Once the temporary Users arrayList has all the stored users in it, add
         * the new user to that arrayList if it has a unique username String
         * If new user username is not unique return false
         * Then write back to the database with new data
         * On success return true
         *
         *
         */

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            synchronized (gatekeeper) {
                User newUser = new User(username, password);
                ArrayList<User> users = new ArrayList<>();

                File databaseFile = new File("userDatabase.txt");
                if (databaseFile.exists() == false) {
                    databaseFile.createNewFile();
                } else {
                    fis = new FileInputStream(databaseFile);
                    ois = new ObjectInputStream(fis);
                    users = (ArrayList<User>) ois.readObject();
                }

                boolean uniqueUserName = true;
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        uniqueUserName = false;
                        return false;
                    }
                }

                users.add(newUser);

                fos = new FileOutputStream(databaseFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(users);
                oos.close();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean removeUser(String userID) {
        /*
         * Creates a temporary Users arraylist. Makes a connection to the userDatabase
         * file and retrieves that arrayList of Users from that text file.
         * Once Users ArrayList has all the users, find the one with matching userID
         * and remove that user from the arrayList. Then remove that user from any
         * friend
         * list or blocked list.
         * Remove the User from the ChatID database too
         * If chat has one other user remaining, delete that entire line and the
         * specific chat database
         * If matching username is not found, return false
         * Then re-write the updated values to the user database
         * If successful, return true
         *
         */
        boolean userFound = false;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        PrintWriter pw = null;
        BufferedReader bfr = null;
        try {
            synchronized (gatekeeper) {
                ArrayList<User> tempUser = new ArrayList<>();
                // reading the file with object input stream
                File userfile = new File("userDatabase.txt");
                fis = new FileInputStream(userfile);
                ois = new ObjectInputStream(fis);
                tempUser = (ArrayList<User>) ois.readObject();

                // looping through each user to find the one that matches
                for (int i = 0; i < tempUser.size(); i++) {

                    // looping through each user's friends
                    ArrayList<User> friends = tempUser.get(i).getFriends();
                    if (friends != null) {
                        for (int j = 0; j < friends.size(); j++) {
                            if (friends.get(j).getUserID().equals(userID)) {
                                friends.remove(j);
                                j--;
                            }
                        }
                    }

                    // looping through each user's blocked
                    ArrayList<User> blocked = tempUser.get(i).getBlocked();
                    if (blocked != null) {
                        for (int k = 0; k < blocked.size(); k++) {
                            if (blocked.get(k).getUserID().equals(userID)) {
                                blocked.remove(k);
                                k--;
                            }
                        }
                    }
                    if (tempUser.get(i).getUserID().equals(userID)) {
                        // removing the user and decrementing by 1
                        tempUser.remove(i);
                        userFound = true;
                        i--;
                    }
                }

                // writing back to the user database with updated values
                fos = new FileOutputStream(userfile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(tempUser);

                oos.close();
                ois.close();
            }

            synchronized (gatekeeper) {
                // removing user from chatID database
                File chat = new File("chatIDs.txt");
                FileReader fr = new FileReader(chat);
                bfr = new BufferedReader(fr);

                ArrayList<String> chatIDLines = new ArrayList<>();
                String currentLine = bfr.readLine();
                while (currentLine != null) {
                    chatIDLines.add(currentLine);
                    currentLine = bfr.readLine();
                }

                for (int i = 0; i < chatIDLines.size(); i++) {
                    String[] line = chatIDLines.get(i).split(",");
                    ArrayList<String> updateLine = new ArrayList<>();
                    for (String item : line) {
                        if (!item.equals(userID)) {
                            updateLine.add(item);
                        } else {
                            userFound = true;
                        }
                    }
                    String ans = "";
                    for (String temp : updateLine) {
                        ans += temp + ",";
                    }
                    if (updateLine.size() >= 3) {
                        chatIDLines.set(i, ans.substring(0, ans.length() - 1));
                    } else {
                        chatIDLines.remove(i);
                        i--;
                    }
                }

                FileWriter fw = new FileWriter("chatIDs.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                pw = new PrintWriter(bw);

                for (String s : chatIDLines) {
                    pw.println(s);
                }

                pw.close();
                bfr.close();

                return userFound;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (pw != null) pw.close();
                if (bfr != null) bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean removeUserFromChat(String userID, String chatID) {
        /*
         * Removes the specific user in the ChatID database
         * If there is only one user left in the chat, delete the chat
         * in the chatID database and delete the actual text file representing the chat.
         */
        boolean userDeleted = false;

        PrintWriter pw = null;
        BufferedReader bfr = null;
        try {
            synchronized (gatekeeper) {
                File file = new File("chatIDs.txt");
                FileReader fr = new FileReader(file);
                bfr = new BufferedReader(fr);

                ArrayList<String> chatIDLines = new ArrayList<>();

                String current = bfr.readLine();
                while (current != null) {
                    chatIDLines.add(current);
                    current = bfr.readLine();
                }

                for (int i = 0; i < chatIDLines.size(); i++) {
                    String[] line = chatIDLines.get(i).split(",");
                    ArrayList<String> updateLine = new ArrayList<>();

                    if (line[0].equals(chatID)) {
                        for (String item : line) {
                            if (!item.equals(userID)) {
                                updateLine.add(item);
                            } else {
                                userDeleted = true;
                            }
                        }
                    } else {
                        continue;
                    }

                    String ans = "";
                    for (String temp : updateLine) {
                        ans += temp + ",";
                    }
                    if (updateLine.size() >= 3) {
                        chatIDLines.set(i, ans.substring(0, ans.length() - 1));
                    } else {
                        chatIDLines.remove(i);
                        i--;
                    }

                }

                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                pw = new PrintWriter(bw);
                for (String s : chatIDLines) {
                    pw.println(s);
                }

                pw.close();
                bfr.close();


                return userDeleted;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bfr != null) bfr.close();
                if (pw != null) pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean addUserToChat(String userID, String chatID) {
        /*
         * Must be an existing userID
         * Make sure this user isnt blocked
         * IN the chatID database, add the userID to the end of the line with the chatID
         */
        boolean userAdded = false;
        boolean existingUser = false;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        BufferedReader bfr = null;
        PrintWriter pw = null;
        try {
            synchronized (gatekeeper) {
                File file1 = new File("userDatabase.txt");
                FileInputStream fis = new FileInputStream(file1);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();

                for (User user : users) {
                    if (user.getUserID().equals(userID)) {
                        existingUser = true;
                    }
                }
                ois.close();
            }

            if (!existingUser) {
                return false;
                // user needs to be created first!
            }

            synchronized (gatekeeper) {

                File file = new File("chatIDs.txt");
                FileReader fr = new FileReader(file);
                bfr = new BufferedReader(fr);

                ArrayList<String> chatIDLines = new ArrayList<>();
                String current = bfr.readLine();
                while (current != null) {
                    chatIDLines.add(current);
                    current = bfr.readLine();
                }
                for (int i = 0; i < chatIDLines.size(); i++) {
                    String[] line = chatIDLines.get(i).split(",");
                    ArrayList<String> updateLine = new ArrayList<>();
                    if (line[0].equals(chatID)) {
                        for (String item : line) {
                            updateLine.add(item);
                        }
                        updateLine.add(userID);
                        userAdded = true;
                        String ans = "";
                        for (String temp : updateLine) {
                            ans += temp + ",";
                        }
                        chatIDLines.set(i, ans.substring(0, ans.length() - 1));

                    }
                }

                FileWriter fw = new FileWriter(file);
                pw = new PrintWriter(fw);

                for (String s : chatIDLines) {
                    pw.println(s);
                }
                pw.close();

                bfr.close();

                return userAdded;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (bfr != null) bfr.close();
                if (pw != null) pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<User> userLookup(String name) {
        /*
         * Uses user database
         * Returns a list of users that contain String “name” in their username
         */
        ObjectInputStream ois = null;

        ArrayList<User> ans = new ArrayList<>();
        try {
            synchronized (gatekeeper) {
                File file = new File("userDatabase.txt");
                FileInputStream fr = new FileInputStream(file);
                ois = new ObjectInputStream(fr);

                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserID().contains(name)) {
                        ans.add(users.get(i));
                    }
                }
                ois.close();
            }
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> userViewer() {
        /*
         * Returns a list of all the viewers
         */
        ObjectInputStream ois = null;
        File file = new File("userDatabase.txt");
        try {
            synchronized (gatekeeper) {
                FileInputStream fr = new FileInputStream(file);
                ois = new ObjectInputStream(fr);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ArrayList<String> ans = new ArrayList<>();
                for (User user : users) {
                    ans.add(user.getUsername());
                }
                return ans;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUser(User user) {
        /*
         * User that is passed in has been updated by the client, with the user methods.
         * Make a connection to the Users database, receive the arraylist of users
         * Search for the old user with same userID, then replace it *
         */
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            synchronized (gatekeeper) {
                File file = new File("userDatabase.txt");
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserID().equals(user.getUserID())) {
                        users.set(i, user);
                        break;
                    }
                }


                FileOutputStream fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(users);
                oos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean loginUser(String username, String password, String userID) {
        /*
         * Receives a username and password. Searches for a user with given userID.
         * Then checks if the given username
         * matches with its password in the users database.
         * Returns true if successful, false if incorrect
         */
        ObjectInputStream ois = null;

        boolean login = false;
        try {
            synchronized (gatekeeper) {
                File file = new File("userDatabase.txt");
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserID().equals(userID)
                            && users.get(i).getPassword().equals(password)
                            && users.get(i).getUsername().equals(username)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updateUserProfilePicture(String userID, String base64) {
        /*
         * Extra credit option
         * Updates profile picture given the userID
         */
        File file = new File("userDatabase.txt");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            synchronized (gatekeeper) {
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ois.close();
                for (User user : users) {
                    if (user.getUserID().equals(userID)) {
                        user.setProfilePicturebase64(base64);
                        FileOutputStream fos = new FileOutputStream(file);
                        oos = new ObjectOutputStream(fos);

                        oos.writeObject(users);
                        return true;
                    }
                }

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserProfilePicture(String userID) {
        /*
         * returns a code format of the user profile picture given the user ID
         */
        File file = new File("userDatabase.txt");
        ObjectInputStream ois = null;
        try {
            synchronized (gatekeeper) {
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ois.close();
                for (User user : users) {
                    if (user.getUserID().equals(userID)) {
                        return user.getProfilePicturebase64();
                    }
                }
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean clearUserProfilePicture(String userID) {
        /*
         * Removes the user's profile picture given the user ID
         */
        File file = new File("userDatabase.txt");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            synchronized (gatekeeper) {
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ois.close();
                for (User user : users) {
                    if (user.getUserID().equals(userID)) {
                        user.clearProfilePicturebase64();
                        FileOutputStream fos = new FileOutputStream(file);
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(users);
                        return true;
                    }
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addFriend(String currentUserID, String friendID) {
        /*
         * Adds a friend given the current User's ID and ID of the friend they want to
         * add.
         * Uses a for:each loop to iterate through the users arrayList and find said
         * user.
         */
        File file = new File("userDatabase.txt");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            synchronized (gatekeeper) {
                // Load the users from the file
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ois.close();

                User currentUser = null;
                User friendUser = null;

                // Find the users with currentUserID and friendID
                for (User user : users) {
                    if (user.getUsername().equals(currentUserID)) {
                        currentUser = user;
                    } else if (user.getUsername().equals(friendID)) {
                        friendUser = user;
                    }
                    if (currentUser != null && friendUser != null) {
                        break; // Exit the loop early if both users are found
                    }
                }

                if (currentUser != null && friendUser != null) {
                    // Add friendUser to currentUser's friends list
                    currentUser.addFriends(friendUser);

                    // Save the updated users list back to the file
                    FileOutputStream fos = new FileOutputStream(file);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(users);
                    oos.close();
                    return true;

                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean blockFriend(String currentuserID, String friendID) {
        /*
         * Blocks another user given userID of the current user and the friend (enemy?)
         * they want to block.
         *
         * If the user is found in the user arrayList, which is a copy of the user
         * database,
         * the loop will iterate through and add them to the blocked user list.
         */
        File file = new File("userDatabase.txt");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            synchronized (gatekeeper) {
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                ArrayList<User> users = (ArrayList<User>) ois.readObject();
                ois.close();
                User currentUser = null;
                User friendUser = null;
                for (User user : users) {
                    if (user.getUsername().equals(currentuserID)) {
                        currentUser = user;
                    }
                    if (user.getUsername().equals(friendID)) {
                        friendUser = user;
                    }
                    if (currentUser != null && friendUser != null) {
                        break;
                    }
                }
                if (currentUser != null && friendUser != null) {
                    currentUser.addBlocked(friendUser);
                    FileOutputStream fos = new FileOutputStream(file);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(users);
                    oos.close();
                    return true;
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}