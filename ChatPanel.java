import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    Client client;


    public ChatPanel(JFrame jframe, Client client){
        setLayout(new BorderLayout());
        this.client = client;
        this.displaychatnames();



    }
    private void displaychatnames(){
        JPanel messagelabel = new JPanel();
        messagelabel.setLayout(new BoxLayout(messagelabel, BoxLayout.Y_AXIS));
        add(messagelabel, BorderLayout.WEST);
        JLabel title = new JLabel("Your groupchats");

        JButton chatButton = new JButton("Chat");
        chatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Chat button clicked");
            }
        });
        messagelabel.add(title);
        messagelabel.add(chatButton);
        String[] chats = client.getChatIDs();
        for(String chat : chats){
            JButton chatButton2 = new JButton((String)client.getUsersInChat(chat));
            chatButton2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(chatButton2.toString() + " clicked");
                }
            });
            messagelabel.add(chatButton2);
        }
    }

}
