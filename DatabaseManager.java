import java.util.ArrayList;
import java.io.*;

//abstract is onlly temporrary
public class DatabaseManager /* implements DatabaseManagerInterface*/ {
    public ArrayList<String> getChatIDs(String userID) {

        ArrayList<String> chatIDs = new ArrayList<>();
        try {
            File f = new File("chatIDs.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String currentLine;
            while((currentLine = bfr.readLine()) != null) {

                if(currentLine.contains(userID)) {
                    chatIDs.add(currentLine.split(",")[0]);
                }
                currentLine = bfr.readLine();
            }
            return chatIDs;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> readChat(ArrayList<String> chatIDs) {
        ArrayList<String> Texts = new ArrayList<>();
        try {
            int chatIDLength = chatIDs.size();
            for(int i = 0; i < chatIDLength; i++) {
                File f = new File("TextHistory" + chatIDs.get(i) + ".txt");
            }
            if(f.exists()) {
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line;
                while(line != null) {

                }
            } else {
                f.createNewFile("TextHistory" + chatIDs + ".txt");
            }

        }
    }




    public boolean removeUser(String userID){
        boolean userFound = false;
        try {
            ArrayList<User> tempUser = new ArrayList<>();
            //reading the file with object input stream
            File userfile = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(userfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tempUser = (ArrayList<User>) ois.readObject();

            //looping through each user to find the one that matches
            for(int i=0; i<tempUser.size(); i++){
                if(tempUser.get(i).getUserID().equals(userID)){
                    //removing the user and decrementing by 1
                    tempUser.remove(i);
                    userFound = true;
                    i--;
                }
                //looping through each user's friends
                ArrayList<User> friends = tempUser.get(i).getFriends();
                if(friends != null) {
                    for (int j = 0; j < friends.size(); j++) {
                        if (friends.get(j).getUserID().equals(userID)) {
                            friends.remove(j);
                            j--;
                        }
                    }
                }

                //looping through each user's blocked
                ArrayList<User> blocked = tempUser.get(i).getBlocked();
                if(blocked != null) {
                    for (int k = 0; k < blocked.size(); k++) {
                        if (blocked.get(k).getUserID().equals(userID)) {
                            blocked.remove(k);
                            k--;
                        }
                    }
                }
            }

            //writing back to the user database with updated values
            FileOutputStream fos = new FileOutputStream(userfile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tempUser);

            oos.close();
            ois.close();

            //removing user from chatID database
            File chat = new File("chatIDs.txt");
            FileReader fr = new FileReader(chat);
            BufferedReader bfr = new BufferedReader(fr);


            ArrayList<String> chatIDLines = new ArrayList<>();
            String currentLine = bfr.readLine();
            while(currentLine!=null){
                chatIDLines.add(currentLine);
                currentLine = bfr.readLine();
            }

            for(int i = 0; i<chatIDLines.size(); i++){
                 String[] line = chatIDLines.get(i).split(",");
                 ArrayList<String> updateLine = new ArrayList<>();
                 for (String item : line) {
                     if(!item.equals(userID)){
                         updateLine.add(item);
                     } else{
                         userFound = true;
                     }
                 }
                 String ans = "";
                 for(String temp : updateLine){
                     ans+=temp+",";
                 }
                 if(updateLine.size() >=3) {
                     chatIDLines.set(i, ans.substring(0, ans.length() - 1));
                 }
                 else {
                     chatIDLines.remove(i);
                     i--;
                 }
            }

            FileWriter fw = new FileWriter("chatIDs.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            for(String s : chatIDLines){
                pw.println(s);
            }

            pw.close();
            bfr.close();

            return userFound;

        } catch(Exception e){
            e.printStackTrace();
            return false;
        }



    }

    public boolean removeUserFromChat(String userID, String chatID){
        boolean userDeleted = false;
        try {
            File file = new File("chatIDs.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            ArrayList<String> chatIDLines = new ArrayList<>();

            String current =  bfr.readLine();
            while(current!=null){
                chatIDLines.add(current);
                current = bfr.readLine();
            }

            for(int i = 0; i<chatIDLines.size(); i++){
                String[] line = chatIDLines.get(i).split(",");
                ArrayList<String> updateLine = new ArrayList<>();

                if(line[0].equals(chatID)) {
                    for (String item : line) {
                        if (!item.equals(userID)) {
                            updateLine.add(item);
                        } else{
                            userDeleted = true;
                        }
                    }
                } else{
                    continue;
                }

                String ans = "";
                for(String temp : updateLine){
                    ans+=temp+",";
                }
                if(updateLine.size() >=3) {
                    chatIDLines.set(i, ans.substring(0, ans.length() - 1));
                }
                else {
                    chatIDLines.remove(i);
                    i--;
                }

            }

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            for(String s : chatIDLines){
                pw.println(s);
            }

            pw.close();
            bfr.close();






            return userDeleted;




        } catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }


    public boolean addUserToChat(String userID, String chatID){
        boolean userAdded = false;
        boolean existingUser = false;
        try {

            File file1 = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file1);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();

            for(User user : users){
                if(user.getUserID().equals(userID)){
                    existingUser = true;
                }
            }
            if(!existingUser){
                return false;
                //user needs to be created first!
            }

            ois.close();

            File file = new File("chatIDs.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            ArrayList<String> chatIDLines = new ArrayList<>();
            String current =  bfr.readLine();
            while(current!=null){
                chatIDLines.add(current);
                current = bfr.readLine();
            }
            for(int i = 0; i<chatIDLines.size(); i++){
                String[] line = chatIDLines.get(i).split(",");
                ArrayList<String> updateLine = new ArrayList<>();
                if(line[0].equals(chatID)) {
                    for (String item : line) {
                        updateLine.add(item);
                    }
                    updateLine.add(userID);
                    userAdded = true;
                    String ans="";
                    for(String temp : updateLine){
                        ans += temp+",";
                    }
                    chatIDLines.set(i, ans.substring(0, ans.length() - 1));

                }
            }

            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            for(String s : chatIDLines){
                pw.println(s);
            }
            pw.close();




            bfr.close();
            return userAdded;


        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public ArrayList<User> userLookup(String name){
        ArrayList<User> ans = new ArrayList<>();
        try {
            File file = new File("userDatabase.txt");
            FileInputStream fr = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fr);

            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            for(int i=0; i<users.size(); i++){
                if(users.get(i).getUserID().equals(name)){
                    ans.add(users.get(i));
                }
            }
            ois.close();
            return ans;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<User> userViewer(){

        File file = new File("userDatabase.txt");
        try{
            FileInputStream fr = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fr);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            return users;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(User user){
        try{
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            for(int i=0; i<users.size(); i++){
                if(users.get(i).getUserID().equals(user.getUserID())){
                    users.set(i, user);
                    break;
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean loginUser(String username, String password, String userID){
        boolean login = false;
        try{
            File file = new File("userDatabase.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> users = (ArrayList<User>) ois.readObject();
            for(int i=0; i<users.size(); i++){
                if(users.get(i).getUserID().equals(userID)
                        && users.get(i).getPassword().equals(password)
                        && users.get(i).getUsername().equals(username)){
                    return true;
                }
            }
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //just for testing purposes






}
