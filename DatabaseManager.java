import java.io.*;
import java.util.*;

// Abstract is only temporary
public class DatabaseManager implements DatabaseManagerInterface {

    public ArrayList<String> getChatIDs(String userID) {
        ArrayList<String> chatIDs = new ArrayList<>();
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

    public String getUsersinChat(String chatID) {
        ArrayList<String> userIDs = new ArrayList<>();
        File file = new File("chatIDs.txt");
        try{
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine = bfr.readLine();
            while ((currentLine) != null) {
                if (currentLine.contains(chatID)) {
                    return currentLine.substring(currentLine.indexOf(",")+1);
                }
            }

            return null;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }



    public ArrayList<String> readChat(String chatID) {
        ArrayList<String> Texts = new ArrayList<>();
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

    public void newText(String currentUserID, String chatID, String message) {

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

    public void deleteText(String chatID, int index) {
        // Make a connection with the file.
        // Run readChat to get an ArrayList<String> of all the messages.
        // Using the passed-in index, delete the corresponding line in the database.
        // Rewrite the entire database with the updated ArrayList values.

            ArrayList<String> Texts = new ArrayList<>();
            try {
                File f = new File(chatID + ".txt");
                if (f.exists()) {
                    FileReader fr = new FileReader(f);
                    BufferedReader bfr = new BufferedReader(fr);
                    String line;
                    while ((line = bfr.readLine()) != null) {
                        Texts.add(line);
                    }
                    if(index < 0 || index >= Texts.size()) {
                        return; //invalid index to delete method
                    }
                    Texts.remove(index);
                    bfr.close();

                    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
                    PrintWriter pw = new PrintWriter(writer);
                        for (String text : Texts) {
                            pw.println(text);
                        }
                    pw.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public void createChat(String[] userID) {
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


    public boolean createUser(String username, String password) {
        // Create a new user with the given username, password, and userID.
        // Create a temporary Users ArrayList.
        // Make a connection to the userDatabase file and retrieve an ArrayList of Users from that text file.
        // Add the new user to the temporary ArrayList if the username is unique.
        // If the username is not unique, return false.
        // Write back to the database with the updated data.
        // On success, return true.
        try {
            User newUser = new User(username, password);
            ArrayList<User> users = new ArrayList<>();

            File databaseFile = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(databaseFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            users = (ArrayList<User>) ois.readObject();

            boolean uniqueUserName=true;
            for(User user : users) {
                if(user.getUsername().equals(username)) {
                    uniqueUserName=false;
                    return false;
                }
            }

            users.add(newUser);

            FileOutputStream fos = new FileOutputStream(databaseFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            return true;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public boolean removeUser(String userID) {
        boolean userFound = false;
        try {
            ArrayList<User> tempUser = new ArrayList<>();
            File userfile = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(userfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tempUser = (ArrayList<User>) ois.readObject();

            for (int i = 0; i < tempUser.size(); i++) {
                if (tempUser.get(i).getUserID().equals(userID)) {
                    tempUser.remove(i);
                    userFound = true;
                    i--;
                }
                ArrayList<User> friends = tempUser.get(i).getFriends();
                for (int j = 0; j < friends.size(); j++) {
                    if (friends.get(j).getUserID().equals(userID)) {
                        friends.remove(j);
                        j--;
                    }
                }

                ArrayList<User> blocked = tempUser.get(i).getBlocked();
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

    public boolean removeUserFromChat(String userID, String chatID) {
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

    public boolean addUserToChat(String userID, String chatID) {
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

    public ArrayList<User> userLookup(String name) {
        ArrayList<User> ans = new ArrayList<>();
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            for (User user : users) {
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

    public ArrayList<User> userViewer() {
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ois.close();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(User user) {
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
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

    public boolean loginUser(String username, String password, String userID) {
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ois.close();

            for (User user : users) {
                if (user.getUserID().equals(userID)
                        && user.getPassword().equals(password)
                        && user.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserProfilePicture(String userID, String base64){
        File file = new File("userDatabase.txt");
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ois.close();
            for (User user : users) {
                if (user.getUserID().equals(userID)){
                    user.setProfilePicturebase64(base64);
                    return true;
                }
            }

            return false;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getUserProfilePicture(String userID){
        File file = new File("userDatabase.txt");
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ois.close();
            for (User user : users) {
                if (user.getUserID().equals(userID)){
                    return user.getProfilePicturebase64();
                }
            }
            return null;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean clearUserProfilePicture(String userID){
        File file = new File("userDatabase.txt");
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            ois.close();
            for (User user : users) {
                if (user.getUserID().equals(userID)){
                    user.clearProfilePicturebase64();
                    return true;
                }
            }
            return false;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean addFriend(String currentUserID, String friendID) {
        File file = new File("userDatabase.txt");

        try {
            // Load the users from the file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
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

    public boolean blockFriend(String currentuserID, String friendID) {
        File file = new File("userDatabase.txt");
        try{
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
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
