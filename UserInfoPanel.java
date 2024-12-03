import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class UserInfoPanel extends JPanel {
    Client client;
    JPanel cardPanel;
    CardLayout cl;
    JFrame frame;

    public UserInfoPanel(JFrame jframe, Client client, JPanel cardPanel, CardLayout cl) {
        setLayout(new BorderLayout());
        this.client = client;
        this.cardPanel = cardPanel;
        this.cl = cl;
        this.frame = jframe;

        // North Panel
        JLabel label = new JLabel("User Profile", JLabel.CENTER);
        add(label, BorderLayout.NORTH);
        this.dispayUserInfoAndButtons();
        this.displayScollingPanes();
        this.dispayToggleButtons();
    }

    // West Panel for Username
    private JLabel imageLabel = new JLabel();
    private void dispayUserInfoAndButtons() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());

        // Base64 string of the profile picture (replace with your Base64 string)
        String base64String = client.getUserProfilePicture(client.getUsername());

        if(client.getUserProfilePicture(client.getUsername()).equals("null") || client.getUserProfilePicture(client.getUsername()).equals("")) {
            client.updateUserProfilePicture(client.getUsername(), "/9j/4AAQSkZJRgABAQAAAQABAAD/" +
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
            base64String = client.getUserProfilePicture(client.getUsername());
            System.out.println(base64String);
        } else {
            base64String = client.getUserProfilePicture(client.getUsername());
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64String);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bufferedImage != null) {
            ImageIcon imageIcon = new ImageIcon(bufferedImage);
            imageLabel = new JLabel(imageIcon);
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));
            profilePanel.add(imageLabel, BorderLayout.NORTH);
        }

        JButton changeProfileButton = new JButton("Change Profile");
        profilePanel.add(changeProfileButton, BorderLayout.CENTER);
        changeProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPic = JOptionPane.showInputDialog(null, "Please enter the Base64 of your image",
                        "Change Info ", JOptionPane.QUESTION_MESSAGE);
                if (newPic == null || newPic.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Profile Picture change cancelled",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                client.updateUserProfilePicture(client.getUsername(), newPic);
                byte[] updatedImageBytes = Base64.getDecoder().decode(newPic);
                BufferedImage updatedBufferedImage = null;
                try {
                     updatedBufferedImage = ImageIO.read(new ByteArrayInputStream(updatedImageBytes));
                } catch (IOException f) {
                    f.printStackTrace();
                }

                    ImageIcon imageIcon = new ImageIcon(updatedBufferedImage);
                    imageLabel.setIcon(imageIcon);

            }
        });

        westPanel.add(profilePanel, BorderLayout.NORTH);

        JLabel usernameLabel = new JLabel("Username: " + client.getUsername(), JLabel.CENTER);
        westPanel.add(usernameLabel, BorderLayout.CENTER);

        JButton changePasswordButton = new JButton("Change Password");
        westPanel.add(changePasswordButton, BorderLayout.SOUTH);
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog(null, "What would you like your new password to be?",
                        "Change Info ", JOptionPane.QUESTION_MESSAGE);
                if (newPassword == null || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password change cancelled",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (newPassword.contains(",") || newPassword.contains(":")) {
                    JOptionPane.showMessageDialog(null, "Password cannot contain commas or colons",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                client.updateUser(client.getUsername(), newPassword);
            }
        });



        add(westPanel, BorderLayout.WEST);
    }

    // East Panel for Scrolling Panes
    private void displayScollingPanes() {

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());

        JPanel dropdownsPanel = new JPanel();
        dropdownsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel label1 = new JLabel("My Followers:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        dropdownsPanel.add(label1, gbc);

        JLabel label2 = new JLabel("Users:");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        dropdownsPanel.add(label2, gbc);

        String[] friendsData = client.getFriends(client.getUsername());
        JList<String> list1 = new JList<>(friendsData);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane1 = new JScrollPane(list1);
        scrollPane1.setPreferredSize(new Dimension(150, 200));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        dropdownsPanel.add(scrollPane1, gbc);

        list1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFriend = list1.getSelectedValue();
                    System.out.println("User wants to visit this friend page: " + selectedFriend);
                    JPanel newFriendProfile = new OtherUserInfoPanel(client, cardPanel, cl, selectedFriend, frame);
                    cardPanel.add(newFriendProfile, "otherUserInfo");
                    cl.show(cardPanel,"otherUserInfo");
                }
            }
        });

        String[] userData = client.userViewer();
        JList<String> list2 = new JList<>(userData);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane2 = new JScrollPane(list2);
        scrollPane2.setPreferredSize(new Dimension(150, 200));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        dropdownsPanel.add(scrollPane2, gbc);

        list2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedUser = list2.getSelectedValue();
                    System.out.println("User wants to visit this user page: " + selectedUser);
                    JPanel newUserProfile = new OtherUserInfoPanel(client, cardPanel, cl, selectedUser, frame);
                    cardPanel.add(newUserProfile, "otherUserInfo");
                    cl.show(cardPanel,"otherUserInfo");
                    cl.show(cardPanel,"otherUserInfo");
                }
            }
        });

        eastPanel.add(dropdownsPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
    }

    // South Panel for Toggle Buttons
    public void dispayToggleButtons() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JToggleButton profileButton = new JToggleButton("My Profile",true);
        bottomPanel.add(profileButton);

        JToggleButton chatButton = new JToggleButton("Chats");
        bottomPanel.add(chatButton);

        chatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("user wants to go to chat page");
                profileButton.setSelected(false);
                cl.show(cardPanel,"chat");
            }
        });

        add(bottomPanel, BorderLayout.SOUTH);
    }


}
