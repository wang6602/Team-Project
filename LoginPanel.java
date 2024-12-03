import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ImagePanel extends JPanel {
    Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        double panelAspect = (double) panelWidth / panelHeight;
        double imageAspect = (double) imageWidth / imageHeight;

        int drawWidth;
        int drawHeight;

        if (panelAspect > imageAspect) {
            drawHeight = panelHeight;
            drawWidth = (int) (imageAspect * drawHeight);
        } else {
            drawWidth = panelWidth;
            drawHeight = (int) (drawWidth / imageAspect);
        }

        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g.drawImage(image, x, y, drawWidth, drawHeight, this);
    }
}

public class LoginPanel extends JPanel {
    Client client;
    JPanel cardPanel;
    CardLayout cardLayout;

    public LoginPanel(JFrame jframe, Client client, JPanel cardPanel, CardLayout cl) {
        this.client = client;
        this.cardPanel = cardPanel;
        this.cardLayout = cl;

        setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("Star Chat.png");
        Image image = imageIcon.getImage();

        JPanel imagePanel = new ImagePanel(image);
        add(imagePanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton createUserButton = new JButton("Create User");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(createUserButton);

        add(formPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
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
                    JPanel otherUserInfo = new OtherUserInfoPanel(client, cardPanel, cl, s, jframe);

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
