package currency.gui;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel(Frame parentFrame) {
        setLayout(new BorderLayout());

        JPanel addCurrencyPanel = new JPanel();
        JLabel currencyLabel = new JLabel("Currency:");
        JTextField currencyField = new JTextField(10);
        JLabel rateLabel = new JLabel("Exchange Rate:");
        JTextField rateField = new JTextField(10);
        JButton addCurrencyButton = new JButton("Add Currency");
        addCurrencyButton.addActionListener(e -> {
            String currency = currencyField.getText();
            double rate = Double.parseDouble(rateField.getText());
            // TODO: Add currency
            JOptionPane.showMessageDialog(null, "Currency added successfully.");
        });

        addCurrencyPanel.add(currencyLabel);
        addCurrencyPanel.add(currencyField);
        addCurrencyPanel.add(rateLabel);
        addCurrencyPanel.add(rateField);
        addCurrencyPanel.add(addCurrencyButton);

        add(addCurrencyPanel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.showCurrencyPanel();
        });
        add(backButton, BorderLayout.SOUTH);
    }
}
