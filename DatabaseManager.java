import java.io.*;
import java.util.*;

// Abstract is only temporary
public class DatabaseManager implements DatabaseManagerInterface {

    public synchronized ArrayList < String > getChatIDs(String userID) {
        /*
        User the userID of the user to return the chat ID’s that the given user is in.
        This method will look through the ChatID Database to retrieve the ChatID’s that
        the user is in.
         */
        ArrayList < String > chatIDs = new ArrayList < > ();
        try {
            File f = new File("chatIDs.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine;
            while ((currentLine = bfr.readLine()) != null) {
                if (currentLine.contains(userID)) {
                    chatIDs.add(currentLine.split(",")[0]);
                }
            }
            bfr.close();
            return chatIDs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized String getUsersinChat(String chatID) {
        /*
        Returns a comma separated value of the various usernames in given chatID
        using the ChatID database file. Useful for the current user to know who he
        is sending a message to.
         */
        ArrayList < String > userIDs = new ArrayList < > ();
        File file = new File("chatIDs.txt");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine = bfr.readLine();
            while ((currentLine) != null) {
                if (currentLine.contains(chatID)) {
                    return currentLine.substring(currentLine.indexOf(",") + 1);
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



    public synchronized ArrayList < String > readChat(String chatID) {
        /*
        Makes a connection to the text file of the given chat. Loops through each line,
         storing each chat line into a String arrayList. Ex [“UserID1A,Hello”,”UserID2,Hello”...
         */
        ArrayList < String > Texts = new ArrayList < > ();
        try {
            File f = new File(chatID + ".txt");
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line;
                while ((line = bfr.readLine()) != null) {
                    Texts.add(line);
                }
                bfr.close();
            }

            return Texts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void newText(String currentUserID, String chatID, String message) {
        /*
        Makes a connection to the given chatID, then adds a new line to the database of UserID,message
        Ensure that this method properly locks the file when writing to avoid concurrency issues if multiple users
        are sending messages simultaneously, likely using synchronized(obj)
         */
        try {
            File f = new File(chatID + ".txt");
            FileOutputStream fos = new FileOutputStream(f, true); // Append mode
            PrintWriter pw = new PrintWriter(fos);
            pw.println(currentUserID + "," + message);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void deleteText(String chatID, int index) {
        /*
        Make a connection with the file. Runs the readChat to get an arrayList<String> of
        all the messages. Using the passed in index, it deletes the according line
        in the database. Then rewrites the whole database with ArrayList values
         */
        ArrayList < String > Texts = new ArrayList < > ();
        try {
            File f = new File(chatID + ".txt");
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line;
                while ((line = bfr.readLine()) != null) {
                    Texts.add(line);
                }
                if (index < 0 || index >= Texts.size()) {
                    return; //invalid index to delete method
                }
                Texts.remove(index);
                bfr.close();

                BufferedWriter writer = new BufferedWriter(new FileWriter(f));
                PrintWriter pw = new PrintWriter(writer);
                for (String text: Texts) {
                    pw.println(text);
                }
                pw.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void createChat(String[] userID) {
        // Create a new entry in the ChatID database.
        // Assign a unique chatID for the group, then follow it with all the user IDs involved
        // (ChatID, UserID1, UserID2, UserID3...).
        // Create a new text file with the ChatID representing the actual chat.
        try {
            File f = new File("ChatIDs.txt");
            FileOutputStream fos = new FileOutputStream(f, true);
            PrintWriter pw = new PrintWriter(fos);
            String chatID = UUID.randomUUID().toString();
            String usersInChat = "";
            for (int i = 0; i < userID.length; i++) {
                usersInChat += userID[i] + ",";
            }

            pw.println(chatID + "," + usersInChat.substring(0, usersInChat.length() - 1)); // prints the chatID to the .txt file


            File newFile = new File(chatID + ".txt");
            newFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized boolean createUser(String username, String password) {
        /*
    Creates a new user with given username and password and UserID
    Creates a temporary Users arrayList. Makes a connection to the
    userDatabase file and retrieves that arrayList of Users from that text file.
    Once the temporary Users arrayList has all the stored users in it, add
    the new user to that arrayList if it has a unique username String
    If new user username is not unique return false
    Then write back to the database with new data
    On success return true

         */
        try {
            User newUser = new User(username, password);
            ArrayList < User > users = new ArrayList < > ();

            File databaseFile = new File("userDatabase.txt");
            if(databaseFile.exists() == false) {
                databaseFile.createNewFile();
            }
            FileInputStream fis = new FileInputStream(databaseFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            users = (ArrayList < User > ) ois.readObject();

            boolean uniqueUserName = true;
            for (User user: users) {
                if (user.getUsername().equals(username)) {
                    uniqueUserName = false;
                    return false;
                }
            }

            users.add(newUser);

            FileOutputStream fos = new FileOutputStream(databaseFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public synchronized boolean removeUser(String userID) {
        /*
        Creates a temporary Users arraylist. Makes a connection to the userDatabase
        file and retrieves that arrayList of Users from that text file.
        Once Users ArrayList has all the users, find the one with matching userID
        and remove that user from the arrayList. Then remove that user from any friend
        list or blocked list.
        Remove the User from the ChatID database too
        If chat has one other user remaining, delete that entire line and the
        specific chat database
        If matching username is not found, return false
        Then re-write the updated values to the user database
        If successful, return true

         */
        boolean userFound = false;
        try {
            ArrayList < User > tempUser = new ArrayList < > ();
            File userfile = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(userfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tempUser = (ArrayList < User > ) ois.readObject();

            for (int i = 0; i < tempUser.size(); i++) {
                if (tempUser.get(i).getUserID().equals(userID)) {
                    tempUser.remove(i);
                    userFound = true;
                    i--;
                }
                ArrayList < User > friends = tempUser.get(i).getFriends();
                for (int j = 0; j < friends.size(); j++) {
                    if (friends.get(j).getUserID().equals(userID)) {
                        friends.remove(j);
                        j--;
                    }
                }

                ArrayList < User > blocked = tempUser.get(i).getBlocked();
                for (int k = 0; k < blocked.size(); k++) {
                    if (blocked.get(k).getUserID().equals(userID)) {
                        blocked.remove(k);
                        k--;
                    }
                }
            }

            FileOutputStream fos = new FileOutputStream(userfile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tempUser);

            oos.close();
            ois.close();

            File chat = new File("chatIDs.txt");
            FileReader fr = new FileReader(chat);
            BufferedReader bfr = new BufferedReader(fr);
            FileWriter fw = new FileWriter("chatIDs_temp.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            String currentLine;
            while ((currentLine = bfr.readLine()) != null) {
                if (!currentLine.contains(userID)) {
                    pw.println(currentLine);
                }
            }

            pw.close();
            bfr.close();
            chat.delete();
            new File("chatIDs_temp.txt").renameTo(chat);

            return userFound;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean removeUserFromChat(String userID, String chatID) {
        /*
        Removes the specific user in the ChatID database
        If there is only one user left in the chat, delete the chat
        in the chatID database and delete the actual text file representing the chat.
         */
        boolean userDeleted = false;
        try {
            File file = new File("chatIDs.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            FileWriter fw = new FileWriter("chatIDs_temp.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            String currentLine;
            while ((currentLine = bfr.readLine()) != null) {
                if (currentLine.contains(chatID) && currentLine.contains(userID)) {
                    userDeleted = true;
                    currentLine = currentLine.replace("," + userID, "");
                    pw.println(currentLine);
                } else {
                    pw.println(currentLine);
                }
            }

            pw.close();
            bfr.close();
            file.delete();
            new File("chatIDs_temp.txt").renameTo(file);

            return userDeleted;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean addUserToChat(String userID, String chatID) {
        /*
        Must be an existing userID
        Make sure this user isnt blocked
        IN the chatID database, add the userID to the end of the line with the chatID
         */
        boolean userAdded = false;
        try {
            File file = new File("chatIDs.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            FileWriter fw = new FileWriter("chatIDs_temp.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.contains(chatID)) {
                    if (!line.contains(userID)) {
                        line += "," + userID;
                        userAdded = true;
                    }
                }
                pw.println(line);
            }

            if (!userAdded) {
                pw.println(chatID + "," + userID);
                userAdded = true;
            }

            pw.close();
            bfr.close();
            file.delete();
            new File("chatIDs_temp.txt").renameTo(file);

            return userAdded;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized ArrayList < User > userLookup(String name) {
        /*
        Uses user database
        Returns a list of users that contain String “name” in their username
         */
        ArrayList < User > ans = new ArrayList < > ();
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            for (User user: users) {
                if (user.getUserID().equals(name)) {
                    ans.add(user);
                }
            }
            ois.close();
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ArrayList < User > userViewer() {
        /*
        Returns a list of all the viewers
         */
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void updateUser(User user) {
        /*
        User that is passed in has been updated by the client, with the user methods.
        Make a connection to the Users database, receive the arraylist of users
        Search for the old user with same userID, then replace it *
         */
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(user.getUserID())) {
                    users.set(i, user);
                    break;
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean loginUser(String username, String password, String userID) {
        /*
        Receives a username and password. Searches for a user with given userID.
        Then checks if the given username
        matches with its password in the users database.
        Returns true if successful, false if incorrect
         */
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();

            for (User user: users) {
                if (user.getUserID().equals(userID) &&
                        user.getPassword().equals(password) &&
                        user.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateUserProfilePicture(String userID, String base64) {
        /*
        Extra credit option
        Updates profile picture given the userID
         */
        File file = new File("userDatabase.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();
            for (User user: users) {
                if (user.getUserID().equals(userID)) {
                    user.setProfilePicturebase64(base64);
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized String getUserProfilePicture(String userID) {
        /*
        returns a code format of the user profile picture given the user ID
         */
        File file = new File("userDatabase.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();
            for (User user: users) {
                if (user.getUserID().equals(userID)) {
                    return user.getProfilePicturebase64();
                }
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean clearUserProfilePicture(String userID) {
        /*
        Removes the user's profile picture given the user ID
         */
        File file = new File("userDatabase.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();
            for (User user: users) {
                if (user.getUserID().equals(userID)) {
                    user.clearProfilePicturebase64();
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public synchronized boolean addFriend(String currentUserID, String friendID) {
        /*
        Adds a friend given the current User's ID and ID of the friend they want to add.
        Uses a for:each loop to iterate through the users arrayList and find said user.
         */
        File file = new File("userDatabase.txt");

        try {
            // Load the users from the file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();

            User currentUser = null;
            User friendUser = null;

            // Find the users with currentUserID and friendID
            for (User user: users) {
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
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(users);
                oos.close();
                return true;


            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean blockFriend(String currentuserID, String friendID) {
        /*
        Blocks another user given userID of the current user and the friend (enemy?)
        they want to block.

        If the user is found in the user arrayList, which is a copy of the user database,
        the loop will iterate through and add them to the blocked user list.
         */
        File file = new File("userDatabase.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList < User > users = (ArrayList < User > ) ois.readObject();
            ois.close();
            User currentUser = null;
            User friendUser = null;
            for (User user: users) {
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
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(users);
                oos.close();
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}