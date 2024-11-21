import javax.swing.*;
import java.awt.*;

public class UserInfoPanel extends JPanel {
    Client client;


    public UserInfoPanel(JFrame jframe, Client client){
        setLayout(new BorderLayout());
        this.client = client;
        JLabel label = new JLabel("Login");
        add(label);
    }
}
