import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInfoPanel extends JPanel {
    Client client;

    public UserInfoPanel(JFrame jframe, Client client) {
        setLayout(new BorderLayout());
        this.client = client;
        // North Panel
        JLabel label = new JLabel("User Profile", JLabel.CENTER);
        add(label, BorderLayout.NORTH);
        this.dispayUserInfoAndButtons();
        this.displayScollingPanes();
        this.dispayToggleButtons();
    }

    // West Panel for Username
    private void dispayUserInfoAndButtons() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());

        JButton changeProfileButton = new JButton("Change Profile");
        westPanel.add(changeProfileButton, BorderLayout.NORTH);

        JLabel usernameLabel = new JLabel("Username: " + client.getUsername(), JLabel.CENTER);
        westPanel.add(usernameLabel, BorderLayout.CENTER);

        JButton changePasswordButton = new JButton("Change Password");
        westPanel.add(changePasswordButton, BorderLayout.SOUTH);
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog(null, "What would you like your new password to be?",
                        "Change Info ", JOptionPane.QUESTION_MESSAGE);
                if (newPassword.contains(",") || newPassword.contains(":")) {
                    JOptionPane.showMessageDialog(null, "Password cannot contain commas or colons",
                            "Error", JOptionPane.ERROR_MESSAGE);
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

        JLabel label1 = new JLabel("My Friends:");
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
        JScrollPane scrollPane1 = new JScrollPane(list1);
        scrollPane1.setPreferredSize(new Dimension(150, 200));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        dropdownsPanel.add(scrollPane1, gbc);

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
                    System.out.println("User wants to visit this user page" + selectedUser);
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
            }
        });

        add(bottomPanel, BorderLayout.SOUTH);
    }


}
