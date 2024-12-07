import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;


public class MainScreen {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client client = new Client();
                //delete this line later - just for testing, userID should be set in login page
                //client.setUserIDandUsername("user1");
                JFrame frame = new JFrame("Main Screen");

                frame.setSize(1000, 900);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);

                JPanel cardPanel = new JPanel(new CardLayout());
                CardLayout cl = (CardLayout) cardPanel.getLayout();

                JPanel login = new LoginPanel(frame, client, cardPanel, cl);


                cardPanel.add(login, "login");


                cl.show(cardPanel, "login");

                frame.add(cardPanel);

                frame.setVisible(true);


            }
        });

    }

}
