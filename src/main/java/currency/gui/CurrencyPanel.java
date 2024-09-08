package currency.gui;

import currency.Main;
import currency.currency.Currency;
import currency.user.Role;

import javax.swing.*;
import java.awt.*;

public class CurrencyPanel extends JPanel {

    public CurrencyPanel(Frame parentFrame) {
        setLayout(new BorderLayout());

        JPanel convertPanel = new JPanel();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField(10);
        JComboBox<String> fromCurrency = new JComboBox<>(Main.instance.currencies.stream().map(Currency::getName).toArray(String[]::new));
        JComboBox<String> toCurrency = new JComboBox<>(Main.instance.currencies.stream().map(Currency::getName).toArray(String[]::new));
        JButton convertButton = new JButton("Convert");

        convertButton.addActionListener(e -> {
            Currency from = Currency.getCurrencyByName((String) fromCurrency.getSelectedItem());
            double amount = Double.parseDouble(amountField.getText());
            String to = (String) toCurrency.getSelectedItem();

            if (from != null && from.getExchangeRates().containsKey(to)) {
                double exchangeRate = from.getExchangeRates().get(to);
                double convertedAmount = amount * exchangeRate;
                JOptionPane.showMessageDialog(null, "Converted Amount: " + convertedAmount);
            } else {
                JOptionPane.showMessageDialog(null, "Error: Currency conversion not available for selected currencies.");
            }
        });

        convertPanel.add(amountLabel);
        convertPanel.add(amountField);
        convertPanel.add(fromCurrency);
        convertPanel.add(toCurrency);
        convertPanel.add(convertButton);

        add(convertPanel, BorderLayout.NORTH);

        String[] columns = new String[5];
        columns[0] = "From/To";
        System.arraycopy(Main.instance.currencies.stream().map(Currency::getName).limit(4).toArray(String[]::new), 0, columns, 1, 4);

        String[][] data = new String[4][5];

        for (int i = 0; i < 4; i++) {
            data[i][0] = columns[i + 1];
            Currency currencyFrom = Currency.getCurrencyByName(columns[i + 1]);
            for (int j = 1; j <= 4; j++) {
                if (i == j - 1) {
                    data[i][j] = "-";
                } else {
                    Currency currencyTo = Currency.getCurrencyByName(columns[j]);
                    if (currencyFrom != null && currencyTo != null) {
                        Double exchangeRate = currencyFrom.getExchangeRates().get(currencyTo.getName());

                        if (exchangeRate != null) {
                            //TODO: Check if rate increased or decreased
                            data[i][j] = String.format("%.2f", exchangeRate);
                        } else {
                            data[i][j] = "";
                        }
                    } else {
                        data[i][j] = "";
                    }
                }
            }
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        if (Main.instance.loggedInUser.getRole() == Role.ADMIN) {
            JButton adminButton = new JButton("Admin");
            adminButton.addActionListener(e -> {
                parentFrame.showAdminPanel();
            });
            add(adminButton, BorderLayout.SOUTH);
        }
    }
}
