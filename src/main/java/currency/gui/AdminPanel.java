package currency.gui;

import currency.Main;
import currency.currency.Currency;
import currency.data.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends JPanel {

    private Map<String, JTextField> exchangeRateFields; // Store text fields for exchange rates

    public AdminPanel(Frame parentFrame) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel addCurrencyPanel = new JPanel();
        JLabel currencyLabel = new JLabel("Currency:");
        JTextField currencyField = new JTextField(10);
        addCurrencyPanel.add(currencyLabel);
        addCurrencyPanel.add(currencyField);

        // Create a panel for exchange rates input
        JPanel exchangeRatesPanel = new JPanel(new GridLayout(0, 2));
        exchangeRateFields = new HashMap<>();

        // For each existing currency, add a label and text field to input the exchange rate
        for (Currency existingCurrency : Main.instance.currencies) {
            JLabel label = new JLabel("Exchange rate to " + existingCurrency.getName() + ":");
            JTextField rateField = new JTextField(10);
            exchangeRateFields.put(existingCurrency.getName(), rateField);

            exchangeRatesPanel.add(label);
            exchangeRatesPanel.add(rateField);
        }

        JButton addCurrencyButton = new JButton("Add Currency");

        addCurrencyButton.addActionListener(e -> {
            String newCurrencyName = currencyField.getText().trim();

            if (newCurrencyName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a currency name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the currency already exists
            if (Currency.getCurrencyByName(newCurrencyName) != null) {
                JOptionPane.showMessageDialog(this, "Currency already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Create the new currency object
                Currency newCurrency = new Currency(newCurrencyName);

                // Add exchange rates for the new currency
                for (Map.Entry<String, JTextField> entry : exchangeRateFields.entrySet()) {
                    String existingCurrencyName = entry.getKey();
                    String rateText = entry.getValue().getText().trim();
                    if (!rateText.isEmpty()) {
                        try {
                            double rate = Double.parseDouble(rateText);

                            // Add the exchange rate and the reciprocal exchange rate
                            newCurrency.getExchangeRates().put(existingCurrencyName, rate);

                            Currency existingCurrency = Currency.getCurrencyByName(existingCurrencyName);
                            if (existingCurrency != null) {
                                existingCurrency.getExchangeRates().put(newCurrencyName, 1 / rate); // Add reciprocal rate
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid exchange rate for " + existingCurrencyName + ". Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Add the new currency to the main list
                Main.instance.currencies.add(newCurrency);
                new Parser().updateJson();
                Main.instance.rateHistoryManager.appendRateHistory(newCurrency);
                JOptionPane.showMessageDialog(this, "Currency added successfully.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton configureCurrencyButton = new JButton("Configure Currencies...");
        configureCurrencyButton.addActionListener(e -> {
            parentFrame.showConfigureCurrencyPanel();
        });


        addCurrencyPanel.add(addCurrencyButton);
        addCurrencyPanel.add(configureCurrencyButton);
        add(addCurrencyPanel, BorderLayout.NORTH);
        add(exchangeRatesPanel, BorderLayout.CENTER);


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.showCurrencyPanel();
        });
        add(backButton, BorderLayout.SOUTH);
    }
}
