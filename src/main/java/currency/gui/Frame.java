package currency.gui;

import javax.swing.*;

public class Frame extends JFrame {

    private JPanel currentPanel;

    public Frame() {
        setTitle("Currency Converter");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        showLoginPanel();
        setVisible(true);
    }

    public void showLoginPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(300, 150);
        currentPanel = new LoginPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showCurrencyPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(400, 200);
        currentPanel = new CurrencyPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showAdminPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(550, 150);
        currentPanel = new AdminPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }
}
