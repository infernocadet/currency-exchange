package currency.gui;

import currency.Main;
import currency.currency.Currency;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ConfigureCurrencyPanel extends JPanel {


    public ConfigureCurrencyPanel(Frame parentFrame) {
        setLayout(new BorderLayout());

        JPanel selectPanel = new JPanel(new GridLayout(5, 1));
        JLabel instructionLabel = new JLabel("Select 4 active currencies");

        // make it a checkbox to click on
        List<Currency> allCurrencies = Main.instance.currencies;
        JCheckBox[] currencyCheckBoxes = new JCheckBox[allCurrencies.size()]; // same size as all loaded currencies
        for (int i = 0; i < allCurrencies.size(); i++) {
            currencyCheckBoxes[i] = new JCheckBox(allCurrencies.get(i).getName());
            selectPanel.add(currencyCheckBoxes[i]);
        }

        // confirmation button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            List<Currency> selectedCurrencies = new ArrayList<>();
            for (int i = 0; i < currencyCheckBoxes.length; i++) {
                if (currencyCheckBoxes[i].isSelected()) {
                    selectedCurrencies.add(allCurrencies.get(i));
                }
            }

            if (selectedCurrencies.size() == 4) {
                Main.instance.activeCurrencies.clear(); // reset any current active currencies
                Main.instance.activeCurrencies.addAll(selectedCurrencies); // then put this selection there
                JOptionPane.showMessageDialog(this, "You have updated the active currencies successfully.");
            } else {
                // need to put 4
                JOptionPane.showMessageDialog(this, "You must select 4 currencies to display.");
            }
        });

        // back button taken from admin panel
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.showAdminPanel();
        });

        add(instructionLabel, BorderLayout.NORTH);
        add(selectPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

