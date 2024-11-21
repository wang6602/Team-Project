import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    Client client;


    public ChatPanel(JFrame jframe, Client client){
        setLayout(new FlowLayout());
        this.client = client;
        JLabel label = new JLabel("chat");
        add(label);
    }
}
