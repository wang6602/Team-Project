import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        DatabaseManager dm = new DatabaseManager();

        File file = new File("userDatabase.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos =  new ObjectOutputStream(fos);
            ArrayList<User> temp = new ArrayList<>();
            for(int i=0; i<=4; i++){
                temp.add(new User(i+"", i+""));
            }

            oos.writeObject(temp);
            oos.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println()




    }
}
