import javax.swing.*;
import java.awt.*;

public class OtherUserInfoPanel extends JPanel {
    Client client;


    public OtherUserInfoPanel(JFrame jframe, Client client){
        setLayout(new GridBagLayout());
        this.client = client;
        JLabel label = new JLabel("otheruserpanel");
        add(label);
    }
}
