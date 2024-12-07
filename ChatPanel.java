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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * <p>
 * ChatPanel holds gui for the chats screen
 * <p>
 *
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @author Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */
public class ChatPanel extends JPanel {
    Client client;
    String currentChat;
    HashMap<String, String> chatIDAndUsers = new HashMap<>();
    JPanel messagelabel = new JPanel();
    JPanel viewChat = new JPanel();
    JFrame jframe;
    CardLayout cl;
    JPanel cardPanel;


    public ChatPanel(JFrame jframe, Client client, JPanel cardPanel, CardLayout cl) {
        this.jframe = jframe;
        setLayout(new BorderLayout());
        this.client = client;
        this.displaychatnamesandchats();
        this.sendMessageandNavigation();
        this.topBarTools();
        this.cl = cl;
        this.cardPanel = cardPanel;


        String[] chats = client.getChatIDs();

        for (int i = 0; i < chats.length; i++) {
            chatIDAndUsers.put(client.getUsersInChat(chats[i]), chats[i]);
        }
        System.out.println(chatIDAndUsers);


    }

    private void displaychatnamesandchats() {

        messagelabel.setLayout(new BoxLayout(messagelabel, BoxLayout.Y_AXIS));
        add(messagelabel, BorderLayout.WEST);
        messagelabel.removeAll();
        messagelabel.revalidate();
        messagelabel.repaint();
        viewChat.removeAll();
        viewChat.revalidate();
        viewChat.repaint();


        viewChat.setLayout(new BoxLayout(viewChat, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Your groupchats:");
        messagelabel.add(title);

        String[] chats = client.getChatIDs();
        if (chats[0].equals("null")) {
            return;
        }

        ButtonGroup chatGroup = new ButtonGroup();
        JPanel chatButtonPanel = new JPanel();
        chatButtonPanel.setLayout(new BoxLayout(chatButtonPanel, BoxLayout.Y_AXIS));


        for (String chat : chats) {
            String usersInChat = (String) client.getUsersInChat(chat);
            chatIDAndUsers.put(usersInChat, chat);
            JToggleButton chatButton2;
            chatButton2 = new JToggleButton(usersInChat);

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
                                        if (result) {
                                            JOptionPane.showMessageDialog(viewChat, "User has been added to the chat");
                                        } else {
                                            JOptionPane.showMessageDialog(viewChat, "User could not be added to the chat", "Error", JOptionPane.ERROR_MESSAGE);
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
                                    if (result) {
                                        JOptionPane.showMessageDialog(viewChat, "User has been removed from the chat");
                                    } else {
                                        JOptionPane.showMessageDialog(viewChat, "User could not be added to the chat", "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                            });

                            chatTools.add(removeUser);


                            chatTools.add(addUser);
                            if (chatContents == null) {
                                JLabel empty = new JLabel("No chat history");
                                add(empty);
                                return;
                            }


                            JPanel profilePic = new JPanel();
                            profilePic.setLayout(new BoxLayout(profilePic, BoxLayout.Y_AXIS));

// Maxi
                            int imageWidth = 40;
                            int imageHeight = 40;

                            String[] users = usersInChat.split(",");
                            for (String user : users) {
                                if (client.getUserProfilePicture(user).equals("null") || client.getUserProfilePicture(user).equals("")) {
                                    client.updateUserProfilePicture(user, "/9j/4AAQSkZJRgABAQAAAQABAAD/" +
                                            "2wCEAAkGBw8PDxANDg0NDxEODQ0PDw8PDRANDw4NFREWFhURFRUYHDQgGBolGxUVITEhJSkrLi4wGB8zODMt" +
                                            "NygtLisBCgoKDg0OGhAQGi0lIB8tLS0tLS0tLSstLystLS0tLS0tLSstLS0tLS0tLS0tKy0tLS0tLS0rLS0tLS0" +
                                            "tLS0tK//AABEIAOEA4QMBEQACEQEDEQH/xAAbAAEAAwEBAQEAAAAAAAAAAAAABAUGAgMBB//EADwQAQACAQEDBwgIBQUA" +
                                            "AAAAAAABAgMRBAUxBhIhQVFhcRMiMlJygZHBM0JDkqGx0eEVU2Ky8CNzgoOi/8QAGQEBAAMBAQAAAAAAAAAAAAAAAAEDBAIF/" +
                                            "8QAKhEBAAICAQMEAQMFAQAAAAAAAAECAxEEEiExEzJBUSJCYXEUI1KBkTP/2gAMAwEAAhEDEQA/AP1dtXgAAAAAAAAAAAAAAAAAA" +
                                            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                                            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPDa9rx4o1yXivZHG0+EOqY7XnVYRqZ8" +
                                            "KfaOUnVjxa9950/CGynBn9UrIxT8oV9/bRPCaV8Kfqvjh44dxih8rv3aI+tWe6aR8ieHjJx1SsHKO0fSY6z30maz8JVX4Mfplz" +
                                            "OH6lcbFvHFm9C3nerbot+7Hkw3x+YVzFq+UtUiJ2JSAAAAAAAAAAAAAAAAAAACJVm996xhjmV6ckxw6qx2z+jRg485O8+HVKzZl" +
                                            "s2W17Te8za08Zl61aRWNQ0REQ4dJAAAfYmY6YnSY4THRMSiYiY1KJaLc2+edMYs0+dPRW/Dnd097zeTxun8qeFGTHrvC8YVcTsS" +
                                            "6AAAAAAAAAAAAAAAAAARt4bXGHHbJPGOisdtuqHeLHOS0VhER1Tpi8uSbWm1p1m0zMz3vbrWKxqGqIiHLpIAAAAAgazcW3+Vx82" +
                                            "0+fj0ie+vVLyOTh9O3bxLNevTKzZ0AAAAAAAAAAAAAAAAAAjbMcptp52SMUcMcdPtz+2j0uFj1Xq+1uKvyp25cAAAAAAAmbp2nyW" +
                                            "atuq0823sz/kfBn5FOuk/s4yRuGzeMoEgAAAAAAAAAAAAAAAD5M6dPZGvuR5cyw205efe95+taZe7jr01iGqsah5LHQAAAAAAADb" +
                                            "7vy8/Djv20jXxjol4WSvTeYZJ92khwkAAAAAAAAAAAAAAAB4bwvzcWS3DTHb46S7xxu8R+6Nd2Hh7rWAAAAAAAAA1fJy+uzxHq2tH" +
                                            "z+byOXGsks94/KVozOQAAAAAAAAAAAAAAAELfVtNnyezEfG0Qu48f3YTX3Qxr2mkAAAAAAAABpeS9v9O8dmSJ+NY/R5fN98fwoyeV0" +
                                            "xuAAAAAAAAAAAAAAAAEDfsa7Pk/4/3Qv40/3YdV9zHvZaAAAAAAAAAGk5LR5mSf6qx79P3eXzvdCjJ7l2xuAAAAAAAAAAAAAAAAEb" +
                                            "eWPnYcle3HaY8YjV3inV4lG+8MTD3WsAAAAAAAABquTePTBr697T+UfJ5HLtvJ/DNed2WrMgAAAAAAAAAAAAAAAAlDm0sTvHZpxZb0" +
                                            "6onWvfWeD28GTrpEtNJ3CMudgAAAAAAOsdJtMViNZtMREd8ubWisblE9o23GyYfJ46Y4+rWI9/W8O9uq0yy+Xq5SAAAAAAAAAAAAAA" +
                                            "AAArd9bt8tXWvp19H+qPVlo4+b07d/EprbpllLUmszW0TExxieiYetFomNw0RO/Dl0kAAAAARI0e4N1zXTNkjSdPMrPGInrl5vK5HV" +
                                            "+FfDPkyb7QvGJyAAAAAAAAAAAAAAAAAAAi7bu/Hmjz69McLR0Wj3rMeW9PEkTMeFNtHJy8fR5K2jst5s/Fspzo/VCyMv2h33NtEfZ6+" +
                                            "Fqz810cvFPy69Sry/hmf+Tf4LP6jF/kn1K/Z/DM/wDJv8D+oxf5HqV+3dNz7RP2Ux4zWHE8rFHyj1K/aXh5PZZ9O9KR3edKq3NrHthE" +
                                            "5fpb7FunFi0mI51vWtpOnhHUx5ORfJ2meyqbTKeoRoSkAAAAAAAAAAAAAAAAAAAAAQjRok0BoQjpB0JAAAAAAAAAAAAAAAAAAAAAAHG" +
                                            "TLWsa2tWsdtpiIIiZ8I2h5d87PX7SLezE2X142Wfh1FbSjX5Q4o4UyT7oj5rI4WT9nXp2K8osXXjyR92fmTwsn3B6dnvj33s88bzX2q" +
                                            "zDi3Fyx8OZrZMw7Tjv6F6W9m0SotS1fMOZ3D1QkAAAAAAAAAAAAAAAAAAAQI227fjwxre3T1VjptPuWY8VsnaIRETaeyg2vf8Alv0Y4j" +
                                            "HHbxt8XoY+FWPd3XVxR8qrJktaeda02ntmdZa61ivaIWa14cukggQkAiZidYmY8J0JiJ8o0stk33mx9Ez5SI6r8fdPFlycSlvHZxbHE+" +
                                            "F/sG9cWboiebb1LcfdPWwZePfH58KLVtCcpNgkAAAAAAAAAAAAAAACZUu9d9xTXHh0tbrvxivh2y2YOLNu9/DqlOrvLOZLzaZm0zMzxm" +
                                            "ZmZl6VaxEahfHbw5dJAAAAAAAETAu9177mumPNMzXhF+Nq+PbDBn4m/wAqf8U3x/MNHW0TGsTExPCYnWJef47Kol9EgAAAAAAAAAAAAC" +
                                            "Bn9+b244cU917R/bDfxuNv87O6U33lQPRXiQAAAAAAAAABabm3rOGYpeZnHM/cntjuY+Tx4vHVXyqvTfeGqraJjWJ1iemJjrh5aqH0AA" +
                                            "AAAAAAAAAAFVv3eHkq8yk+fePu17WnjYfUtufEJpXqllXrQ0iQAAAAAAAAAAABfcnd4/YX4T9HPZPqvO5eD9cf7UZK/MNCwOIkAAAAAA" +
                                            "AAAABxnyxStr24ViZlNazaYiEeZ0xG17ROW9sluNp+EdUPbx0ilYrDVWNRp5LEgAAAAAAAAAAAAPtbTHTHRMTrE9komN9pNbbPde1xmx" +
                                            "Vv9b0bd1oeJmxzjvMMk16bJatIAAAAAAAAACj5T7VpWuGJ9PW1vCOH4/k28LHu02n4dY43O2cem0AAAAAAAAAAAAAAALfk3tPNyzjmej" +
                                            "JH/qOHzYuZj3Xq+lOaNxtqHmKoBIAAAAAAAAE+GN3xn8pnvPVE82PCOh7HGp044X441VCaHYAAAAAAAAAAAAAADvDkmtq2jjWYmPGJc3" +
                                            "r1VmETG4brHeLVi0cLRFo8J6XhTGp0yw6QkAAAAAAABxmvza2t6tbW+EJrG5iEb7sJPbL3ojUNURqHxKQAAAAAAAAAAAAAAAGw3Fk52z" +
                                            "07udX4TpH4aPF5NenLLLbtZPUgAAAAAAACNvL6DL/tX/J3i98fyiPcxMvchrEgAAAAAAAAAAAAAAADVcmvoP8Asv8AJ5HM/wDVmv7pWr" +
                                            "MgAAAB/9k=");
                                }
                                String base64 = client.getUserProfilePicture(user);
                                byte[] imageBytes = Base64.getDecoder().decode(base64);
                                BufferedImage bufferedImage = null;
                                try {
                                    bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }

                                JPanel userPanel = new JPanel();
                                userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                                userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, imageHeight + 10));

                                if (bufferedImage != null) {
                                    Image scaledImage = bufferedImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                                    ImageIcon imageIcon = new ImageIcon(scaledImage);
                                    JLabel imageLabel = new JLabel(imageIcon);
                                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                                    userPanel.add(imageLabel);
                                }


                                JLabel nameLabel = new JLabel(user);
                                nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                                userPanel.add(nameLabel);


                                profilePic.add(userPanel);
                            }
                            //viewChat.add(profilePic);
                            JScrollPane profilePicScroll = new JScrollPane(profilePic);
                            profilePicScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            viewChat.add(profilePicScroll);


                            JPanel messagePanel = new JPanel();
                            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
                            messagePanel.setBackground(Color.WHITE);


                            JScrollPane scrollPane = new JScrollPane(messagePanel);
                            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                            int index = 1;


                            for (int i = 0; i < chatContents.length - 1; i += 2) {
                                String username = chatContents[i];
                                String message = chatContents[i + 1];
                                JLabel messageLabel;

                                if (username.equals(client.getUsername())) {
                                    messageLabel = new JLabel("<html>" + index + ". You" + "<br>" + message + "</html>");
                                    messageLabel.setOpaque(true);
                                    messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));


                                    messageLabel.setBackground(new Color(139, 225, 255)); // Light blue
                                    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                    messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                                } else {
                                    messageLabel = new JLabel("<html>" + index + ". " + username + "<br>" + message + "</html>");
                                    messageLabel.setOpaque(true);
                                    messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                                    messageLabel.setBackground(new Color(240, 240, 240)); //grey
                                    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                    messageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);


                                }


                                messageLabel.setMaximumSize(new Dimension(300, messageLabel.getPreferredSize().height));

                                messagePanel.add(messageLabel);
                                messagePanel.add(Box.createVerticalStrut(10));
                                index++;
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

            chatButtonPanel.add(chatButton2);

        }
        JScrollPane chatScrollPane = new JScrollPane(chatButtonPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messagelabel.add(chatScrollPane);

    }


    private void sendMessageandNavigation() {
        JPanel messageAndNavigation = new JPanel();
        messageAndNavigation.setLayout(new FlowLayout());

        JPanel navigation = new JPanel();
        navigation.setLayout(new FlowLayout());

        JPanel sendMessage = new JPanel();
        sendMessage.setLayout(new FlowLayout());


        JToggleButton userPage = new JToggleButton("My Profile");
        JToggleButton chatPage = new JToggleButton("Chats", true);
        userPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel updatedUserProfile = new UserInfoPanel(jframe, client, cardPanel, cl);
                cardPanel.add(updatedUserProfile, "updatedUserInfo");
                cl.show(cardPanel, "updatedUserInfo");
            }
        });


        navigation.add(userPage);
        navigation.add(chatPage);


        JLabel newmessage = new JLabel("New message");
        JTextField newMessageField = new JTextField(10);
        JButton send = new JButton("Send");

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

                new Thread(() -> {
                    boolean result = client.newText(currentChat, input);


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

    private JLabel createImage(String base64) {
        try {
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


    private void topBarTools() {
        JMenuBar topBar = new JMenuBar();
        topBar.setLayout(new FlowLayout());

        JMenu newChat = new JMenu("New Chat");
        newChat.setLayout(new FlowLayout());

        String[] friends = client.getFriends(client.getUsername());
        ArrayList<String> selectedFriends = new ArrayList<>();

        for (String friend : friends) {
            if (!(friends[0].equals("null"))) {
                JCheckBoxMenuItem newChatMenuItem = new JCheckBoxMenuItem(friend);
                newChat.add(newChatMenuItem);


                newChatMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (newChatMenuItem.isSelected()) {
                            selectedFriends.add(friend);
                        } else {
                            selectedFriends.remove(friend);
                        }
                        newChat.getPopupMenu().setVisible(true);
                    }
                });
            }
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

                String[] selected = new String[selectedFriends.size() + 1];
                selected[0] = client.getUsername();
                for (int i = 0; i < selectedFriends.size(); i++) {
                    selected[i + 1] = selectedFriends.get(i);
                }
                String compare = "";
                for (String str : selected) {
                    compare += str + ",";
                }

                if (selected.length <= 1) {
                    JOptionPane.showMessageDialog(newChat,
                            "Select a user to send a chat to",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (firstMessageField.getText().equals("")) {
                    JOptionPane.showMessageDialog(newChat,
                            "Make sure your first message is not blank",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                compare = compare.substring(0, compare.length() - 1);

                if (chatIDAndUsers.containsKey(compare)) {
                    JOptionPane.showMessageDialog(newChat,
                            "This chat already exists",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    firstMessageField.setText("");

                    return;
                }

                String newChat = client.createChat(selected);
                client.newText(newChat, firstMessageField.getText());
                JOptionPane.showMessageDialog(viewChat, "chat Succesfully Created - reload the page", "Success", JOptionPane.INFORMATION_MESSAGE);
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
                cl.show(cardPanel, "login");
            }
        });

        topBar.add(newChat);
        topBar.add(logout);

        topBar.add(reload);


        add(topBar, BorderLayout.NORTH);
    }

}
