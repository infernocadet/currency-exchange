package currency.gui;

import javax.swing.*;

public class Frame extends JFrame {

    public Frame() {
        setTitle("Currency Converter");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new LoginPanel());

        setVisible(true);
    }
}
