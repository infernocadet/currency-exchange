package currency.gui;

import currency.Main;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(Frame parentFrame) {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        JTextField username = new JTextField();
        add(username);

        add(new JLabel("Password:"));
        JPasswordField password = new JPasswordField();
        add(password);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String usernameText = username.getText();
            String passwordText = new String(password.getPassword());

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username and password");
                return;
            }

            if (Main.instance.login(usernameText, passwordText)) {
                parentFrame.showCurrencyPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        });
        add(loginButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }
}
