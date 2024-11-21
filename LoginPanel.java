import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    Client client;


    public LoginPanel(JFrame jframe, Client client){
        setLayout(new FlowLayout());
        this.client = client;
        JLabel label = new JLabel("Login");
        add(label);
    }
}
