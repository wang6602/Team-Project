import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    Client client;
    JPanel cardPanel;
    CardLayout cardLayout;

    public LoginPanel(JFrame jframe, Client client, JPanel cardPanel, CardLayout cl) {
        setLayout(new FlowLayout());
        this.client = client;
        JLabel label = new JLabel("Login");
        add(label);
        this.cardPanel = cardPanel;
        this.cardLayout = cl;

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton createUserButton = new JButton("Create User");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(createUserButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User myUser = new User(username, password);
                String userID = myUser.getUserID();
                if (username.contains(",") || username.contains(":") || password.contains(",") || password.contains(":")) {
                    JOptionPane.showMessageDialog(jframe,
                            "Username and password cannot contain commas or colons",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (client.loginUser(username, password, username)) {
                    client.setUserIDandUsername(username);
                    JOptionPane.showMessageDialog(jframe,
                            "Welcome back, " + username,
                            "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                    JPanel chat = new ChatPanel(jframe, client, cardPanel, cl);
                    JPanel userInfo = new UserInfoPanel(jframe, client, cardPanel, cl);
                    String s = null;
                    JPanel otherUserInfo = new OtherUserInfoPanel(client, cardPanel, cl, s);

                    cardPanel.add(chat, "chat");
                    cardPanel.add(userInfo, "userInfo");
                    cardPanel.add(otherUserInfo, "otherUserInfo");

                    cardLayout.show(cardPanel, "chat");
                } else {
                    JOptionPane.showMessageDialog(jframe,
                            "Invalid username or password",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.contains(",") || username.contains(":") || password.contains(",") || password.contains(":")) {
                    JOptionPane.showMessageDialog(jframe,
                            "Username and password cannot contain commas or colons",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(jframe,
                            "Password must be at least 8 characters long",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (client.createUser(username, password)) {
                    JOptionPane.showMessageDialog(jframe,
                            "User created successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(jframe,
                            "User already exists",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
