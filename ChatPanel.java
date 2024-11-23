import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ChatPanel extends JPanel {
    Client client;
    String currentChat;
    HashMap<String,String> chatIDAndUsers = new HashMap<>();


    public ChatPanel(JFrame jframe, Client client){
        setLayout(new BorderLayout());
        this.client = client;
        this.displaychatnamesandchats();
        this.sendMessageandNavigation();





    }
    private void displaychatnamesandchats(){
        JPanel messagelabel = new JPanel();
        messagelabel.setLayout(new BoxLayout(messagelabel, BoxLayout.Y_AXIS));
        add(messagelabel, BorderLayout.WEST);

        JPanel viewChat = new JPanel();
        viewChat.setLayout(new BoxLayout(viewChat, BoxLayout.Y_AXIS));

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
            String usersInChat = (String)client.getUsersInChat(chat);
            chatIDAndUsers.put(usersInChat,chat);
            JButton chatButton2 = new JButton(usersInChat);
            chatButton2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(chatButton2.toString() + " clicked");
                    currentChat = chatIDAndUsers.get(chatButton2.getText());

                    viewChat.removeAll();
                    revalidate();
                    repaint();

                    new Thread(() -> {
                        String[] chatContents = client.readChat(chat);

                        SwingUtilities.invokeLater(() -> {

                            JLabel title = new JLabel("Your chat");
                            JLabel text = new JLabel(chatContents[1]);

                            //This is more powerful than Jtextarea
                            JTextPane textPane = new JTextPane();
                            textPane.setEditable(false);
                            //This is for formatting
                            StyledDocument doc = textPane.getStyledDocument();

                            Style leftAlign = textPane.addStyle("Left", null);
                            StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);

                            Style rightAlign = textPane.addStyle("Right", null);
                            StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);

                            try {
                                for (int i = 0; i < chatContents.length - 1; i += 2) {
                                    String username = chatContents[i];
                                    String message = chatContents[i + 1];


                                    if (username.equals(client.getUsername())) {
                                        doc.setParagraphAttributes(doc.getLength(), message.length(), rightAlign, true);
                                        doc.insertString(doc.getLength(), "You:\n", null);
                                        doc.insertString(doc.getLength(), message + "\n\n", null);
                                    } else {
                                        doc.setParagraphAttributes(doc.getLength(), message.length(), leftAlign, true);
                                        doc.insertString(doc.getLength(), username + ":\n", null);
                                        doc.insertString(doc.getLength(), message + "\n\n", null);
                                    }


                                }
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }

                            viewChat.add(title);
                            viewChat.add(text);
                            viewChat.add(textPane);
                            add(viewChat, BorderLayout.CENTER);
                            revalidate();
                            repaint();
                        });
                    }).start();
                }
            });

            messagelabel.add(chatButton2);
        }
    }


    private void sendMessageandNavigation(){
        JPanel messageAndNavigation = new JPanel();
        messageAndNavigation.setLayout(new FlowLayout());

        JPanel navigation = new JPanel();
        navigation.setLayout(new FlowLayout());

        JPanel sendMessage = new JPanel();
        sendMessage.setLayout(new FlowLayout());


        JToggleButton userPage = new JToggleButton("UserProfilePage");
        JToggleButton chatPage = new JToggleButton("ChatPage", true);
        userPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("user wants to go to profile page");
                chatPage.setSelected(false);
            }
        });

        navigation.add(userPage);
        navigation.add(chatPage);

        JLabel newmessage = new JLabel("New message");
        JTextField newMessageField = new JTextField(10);
        JButton send =  new JButton("Send");

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = newMessageField.getText();
                if(input.contains(",") || input.contains(":")){
                    JOptionPane.showMessageDialog(newmessage, "Please make sure that messages do not contain commas or Colons", "Error", JOptionPane.ERROR_MESSAGE);
                    newMessageField.setText("");
                } if(currentChat == null){
                    JOptionPane.showMessageDialog(newmessage, "Please select a chat first", "Error", JOptionPane.ERROR_MESSAGE);
                } else{
                    boolean result = client.newText(currentChat, input);

                    if(!result){
                        JOptionPane.showMessageDialog(newmessage, "Follow these individuals before sending a message or You may have been blocked", "Error", JOptionPane.ERROR_MESSAGE);
                        newMessageField.setText("");
                    } else{
                        JOptionPane.showMessageDialog(newmessage, "You have been sent a message", "Success", JOptionPane.INFORMATION_MESSAGE);
                        newMessageField.setText("");
                    }

                }

            }
        });


        sendMessage.add(newmessage);
        sendMessage.add(newMessageField);
        sendMessage.add(send);

        messageAndNavigation.add(navigation);
        messageAndNavigation.add(sendMessage);
        add(messageAndNavigation, BorderLayout.SOUTH);



    }

}
