import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;


public class MainScreen {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client client = new Client();
                //delete this line later - just for testing, userID should be set in login page
                client.setUserIDandUsername("user1");
                JFrame frame = new JFrame("Main Screen");
                frame.setSize(600,500);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);


                JPanel login = new LoginPanel(frame, client);
                JPanel chat = new ChatPanel(frame, client);
                JPanel userInfo = new UserInfoPanel(frame, client);
                JPanel otherUserInfo = new OtherUserInfoPanel(frame, client);

                JPanel cardPanel = new JPanel(new CardLayout());
                cardPanel.add(login, "login");
                cardPanel.add(chat, "chat");
                cardPanel.add(userInfo, "userInfo");
                cardPanel.add(otherUserInfo, "otherUserInfo");

                CardLayout cl = (CardLayout) cardPanel.getLayout();
                cl.show(cardPanel, "chat");

                frame.add(cardPanel);

                frame.setVisible(true);




            }
        });
    }



}
