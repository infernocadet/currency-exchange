package currency.gui;

import currency.Main;
import currency.currency.Currency;
import currency.data.Parser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRatePanel extends JPanel {

    public ExchangeRatePanel(Frame parentFrame, Currency currency) {
        Map<String, JTextField> exchangeRateFields = new HashMap<>();

        setLayout(new BorderLayout());

        JLabel currencyTitle = new JLabel(String.format("Exchange Rates for %s", currency.getName()), SwingConstants.CENTER);
        add(currencyTitle, BorderLayout.NORTH);

        JPanel containerPanel = new JPanel(new BorderLayout());

        JPanel exchangeRatesPanel = new JPanel(new GridLayout(0, 2));
        for (Map.Entry<String, Double> exchange : currency.getExchangeRates().entrySet()) {
            JLabel exchangeRateLabel = new JLabel(String.format("Exchange rate to %s: ", exchange.getKey()));
            JTextField exchangeRateTextField = new JTextField(exchange.getValue().toString());
            exchangeRateFields.put(exchange.getKey(), exchangeRateTextField);
            exchangeRatesPanel.add(exchangeRateLabel);
            exchangeRatesPanel.add(exchangeRateTextField);
        }

        JLabel dateLabel = new JLabel("Enter date (yyyy-MM-dd): ");
        JTextField dateTextField = new JTextField(LocalDate.now().toString());
        exchangeRatesPanel.add(dateLabel);
        exchangeRatesPanel.add(dateTextField);

        containerPanel.add(exchangeRatesPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDate date = LocalDate.parse(dateTextField.getText(), dateFormatter);
                for (Map.Entry<String, JTextField> exchangeField : exchangeRateFields.entrySet()) {
                    double rate = Double.parseDouble(exchangeField.getValue().getText());
                    if (rate != currency.getExchangeRates().get(exchangeField.getKey())) {
                        currency.getExchangeRates().put(exchangeField.getKey(), rate);
                        new Parser().updateJson();
                        Main.instance.rateHistoryManager.appendRateHistory(currency.getName(), exchangeField.getKey(), rate, date);
                    }
                }
                JOptionPane.showMessageDialog(parentFrame, String.format("Updated exchange rates for %s", currency.getName()));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format (yyyy-MM-dd)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(saveButton);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(containerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.showAdminPanel();
        });
        add(backButton, BorderLayout.SOUTH);
    }
}
