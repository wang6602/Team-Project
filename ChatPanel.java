import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class ChatPanel extends JPanel {
    Client client;
    String currentChat;
    HashMap<String,String> chatIDAndUsers = new HashMap<>();
    JPanel messagelabel = new JPanel();
    JPanel viewChat = new JPanel();
    JFrame jframe;


    public ChatPanel(JFrame jframe, Client client){
        this.jframe = jframe;
        setLayout(new BorderLayout());
        this.client = client;
        this.displaychatnamesandchats();
        this.sendMessageandNavigation();
        this.topBarTools();
    }
    private void displaychatnamesandchats(){

        messagelabel.setLayout(new BoxLayout(messagelabel, BoxLayout.Y_AXIS));
        add(messagelabel, BorderLayout.WEST);
        messagelabel.removeAll();
        messagelabel.revalidate();
        messagelabel.repaint();
        viewChat.removeAll();
        viewChat.revalidate();
        viewChat.repaint();


        viewChat.setLayout(new BoxLayout(viewChat, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Your groupchats");
        messagelabel.add(title);

        String[] chats = client.getChatIDs();

        ButtonGroup chatGroup = new ButtonGroup();

        for(String chat : chats){
            String usersInChat = (String)client.getUsersInChat(chat);
            chatIDAndUsers.put(usersInChat,chat);
            JToggleButton chatButton2 = new JToggleButton(usersInChat);
            chatGroup.add(chatButton2);
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
                            textPane.setPreferredSize(new Dimension(300,300));
                            JScrollPane scrollPane = new JScrollPane(textPane);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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

                                    textPane.setCaretPosition(doc.getLength());


                                }
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }

                            viewChat.add(title);
                            viewChat.add(text);
                            viewChat.add(textPane, BorderLayout.CENTER);
                            viewChat.add(scrollPane, BorderLayout.EAST);

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
                    newMessageField.setText("");
                } else{
                    boolean result = client.newText(currentChat, input);

                    if(!result){
                        JOptionPane.showMessageDialog(newmessage, "Follow these individuals before sending a message or You may have been blocked", "Error", JOptionPane.ERROR_MESSAGE);
                        newMessageField.setText("");
                    } else{
                        JOptionPane.showMessageDialog(newmessage, "You have been sent a message - reload the page", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    private JLabel createImage(String base64){
        try{
            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(bais);

            ImageIcon icon = new ImageIcon(bufferedImage);
            JLabel label = new JLabel(icon);
            return label;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private void topBarTools(){
        JMenuBar topBar = new JMenuBar();
        topBar.setLayout(new FlowLayout());

        JMenu newChat = new JMenu("New Chat");
        newChat.setLayout(new FlowLayout());

        String[] friends = client.getFriends(client.getUsername());
        ArrayList<String> selectedFriends = new ArrayList<>();

        for (String friend : friends) {
            JCheckBoxMenuItem newChatMenuItem = new JCheckBoxMenuItem(friend);
            newChat.add(newChatMenuItem);


            newChatMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (newChatMenuItem.isSelected()) {
                        // Add friend to selected list
                        selectedFriends.add(friend);
                    } else {
                        // Remove friend if unchecked
                        selectedFriends.remove(friend);
                    }
                    newChat.getPopupMenu().setVisible(true);
                }
            });
        }
        JButton createChat = new JButton("Create");
        newChat.add(createChat);
        createChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(selectedFriends.toString());
                String[] selected = new String[selectedFriends.size()+1];
                selected[0] = client.getUsername();
                for(int i = 0; i < selectedFriends.size(); i++){
                    selected[i+1] = selectedFriends.get(i);
                }
                client.createChat(selected);
            }
        });



        JMenuItem reload = new JMenuItem("Reload");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaychatnamesandchats();
            }
        });
        topBar.add(newChat);

        topBar.add(reload);


        add(topBar, BorderLayout.NORTH);
    }

}
