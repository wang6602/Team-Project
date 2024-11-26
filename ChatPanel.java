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
                    viewChat.revalidate();
                    viewChat.repaint();

                    new Thread(() -> {
                        String[] chatContents = client.readChat(chat);

                        SwingUtilities.invokeLater(() -> {
                            JMenuBar chatTools = new JMenuBar();
                            chatTools.setLayout(new FlowLayout());

                            JLabel title = new JLabel("Your chat: ");

                            JMenu deleteTextMenu = new JMenu("Delete Text");
                            JTextField deleteTextIndex = new JTextField(2);
                            JButton deleteButton = new JButton("Delete");

                            chatTools.add(title);
                            chatTools.add(deleteTextMenu);
                            deleteTextMenu.add(deleteTextIndex);
                            deleteTextMenu.add(deleteButton);
                            deleteButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        client.deleteText(chat, Integer.parseInt(deleteTextIndex.getText()) - 1);
                                    } catch (NumberFormatException e1) {
                                        JOptionPane.showMessageDialog(viewChat, "please enter a valid integer associated with the specific text", "Error", JOptionPane.ERROR_MESSAGE);
                                        deleteTextIndex.setText("");
                                    }
                                }
                            });

                            JMenu addUser = new JMenu("Add User");
                            JTextField addUsername = new JTextField(2);
                            addUser.add(addUsername);


                            JButton addUserButton = new JButton("Add User");
                            addUser.add(addUserButton);
                            addUserButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        boolean result = client.addUserToChat(addUsername.getText(), currentChat);
                                        if(result){
                                            JOptionPane.showMessageDialog(viewChat, "User has been added to the chat");
                                        } else{
                                            JOptionPane.showMessageDialog(viewChat, "User could not be added to the chat");
                                        }
                                    } catch (NumberFormatException e1) {
                                        JOptionPane.showMessageDialog(viewChat, "please enter a valid integer associated with the specific text", "Error", JOptionPane.ERROR_MESSAGE);
                                        deleteTextIndex.setText("");
                                    }
                                }
                            });

                            JMenu removeUser = new JMenu("Remove User");
                            String possibleRemove = client.getUsersInChat(currentChat);
                            String[] usersToRemove = possibleRemove.split(",");
                            JTextField removeUserIndex = new JTextField(2);
                            removeUser.add(removeUserIndex);
                            JButton removeUserButton = new JButton("Remove User");
                            removeUser.add(removeUserButton);
                            removeUserButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {

                                        boolean result = client.removeUserFromChat(removeUserIndex.getText(), currentChat);
                                        if(result){
                                            JOptionPane.showMessageDialog(viewChat, "User has been removed from the chat");
                                        } else{
                                            JOptionPane.showMessageDialog(viewChat, "User could not be removed from the chat");
                                        }

                                }
                            });

                            chatTools.add(removeUser);


                            chatTools.add(addUser);
                            if(chatContents == null){
                                JLabel empty = new JLabel("No chat history");
                                add(empty);
                                return;
                            }


                            JTextPane textPane = new JTextPane();
                            textPane.setPreferredSize(new Dimension(300, 300));
                            textPane.setEditable(false);


                            JScrollPane scrollPane = new JScrollPane(textPane);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

// This is for formatting
                            StyledDocument doc = textPane.getStyledDocument();

                            Style leftAlign = textPane.addStyle("Left", null);
                            StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);

                            Style rightAlign = textPane.addStyle("Right", null);
                            StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);

                            try {
                                int index = 0;
                                for (int i = 0; i < chatContents.length - 1; i += 2) {
                                    String username = chatContents[i];
                                    String message = chatContents[i + 1];

                                    if (username.equals(client.getUsername())) {
                                        doc.setParagraphAttributes(doc.getLength(), message.length(), rightAlign, true);
                                        doc.insertString(doc.getLength(), index + 1 + ". You:\n", null);
                                        doc.insertString(doc.getLength(), message + "\n\n", null);
                                    } else {
                                        doc.setParagraphAttributes(doc.getLength(), message.length(), leftAlign, true);
                                        doc.insertString(doc.getLength(), index + 1 + ". " + username + ":\n", null);
                                        doc.insertString(doc.getLength(), message + "\n\n", null);
                                    }
                                    index++;

                                    textPane.setCaretPosition(doc.getLength());
                                }
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }

                            viewChat.add(chatTools, BorderLayout.NORTH);
                            viewChat.add(scrollPane, BorderLayout.CENTER);


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

                if (input.contains(",") || input.contains(":")) {
                    JOptionPane.showMessageDialog(newmessage,
                            "Please make sure that messages do not contain commas or colons",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    newMessageField.setText("");
                    return;
                }

                if (currentChat == null) {
                    JOptionPane.showMessageDialog(newmessage,
                            "Please select a chat first",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    newMessageField.setText("");
                    return;
                }

                // Run client.newText in a separate thread
                new Thread(() -> {
                    boolean result = client.newText(currentChat, input);

                    // Update GUI on the EDT
                    SwingUtilities.invokeLater(() -> {
                        if (!result) {
                            JOptionPane.showMessageDialog(newmessage,
                                    "Follow these individuals before sending a message or you may have been blocked",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(newmessage,
                                    "Your message has been sent - reload the page",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                        }

                        // Clear the message field regardless of the result
                        newMessageField.setText("");
                    });
                }).start();
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

        JLabel firstmessage = new JLabel("First Message");
        JTextField firstMessageField = new JTextField(10);


        JButton createChat = new JButton("Create");
        newChat.add(firstmessage);
        newChat.add(firstMessageField);
        newChat.add(createChat);
        createChat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(selectedFriends.toString());
                String[] selected = new String[selectedFriends.size()+1];
                selected[0] = client.getUsername();
                for(int i = 0; i < selectedFriends.size(); i++){
                    selected[i+1] = selectedFriends.get(i);
                }
                String compare = "";
                for(String str : selected){
                    compare += str+",";
                }
                String duplicate = chatIDAndUsers.get(compare);
                if(duplicate == null){
                    JOptionPane.showMessageDialog(newChat,
                            "This chat already exists",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    firstMessageField.setText("");

                    return;
                }

                String newChat = client.createChat(selected);
                client.newText(newChat, firstMessageField.getText());
            }
        });



        JMenuItem reload = new JMenuItem("Reload");
        reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaychatnamesandchats();
            }
        });

        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logout");
            }
        });

        topBar.add(newChat);
        topBar.add(logout);

        topBar.add(reload);


        add(topBar, BorderLayout.NORTH);
    }

}
