package currency.gui;

import currency.Main;
import currency.currency.Currency;
import currency.data.RateHistoryManager;
import currency.user.Role;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SummaryPanel extends JPanel {

    private JTextField startDateField;
    private JTextField endDateField;
    private JComboBox<String> fromCurrencyCombo;
    private JComboBox<String> toCurrencyCombo;
    private JTextArea resultArea;

    public SummaryPanel(Frame parentFrame) {
        setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(5, 2));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // labels and options added to the panel
        JLabel fromCurrencyLabel = new JLabel("From Currency:");
        fromCurrencyCombo = new JComboBox<>(Main.instance.currencies.stream().map(Currency::getName).toArray(String[]::new));

        JLabel toCurrencyLabel = new JLabel("To Currency:");
        toCurrencyCombo = new JComboBox<>(Main.instance.currencies.stream().map(Currency::getName).toArray(String[]::new));

        JLabel startDateLabel = new JLabel("Start Date (yyyy-MM-dd):");
        startDateField = new JTextField(10);

        JLabel endDateLabel = new JLabel("End Date (yyyy-MM-dd):");
        endDateField = new JTextField(10);


        summaryPanel.add(fromCurrencyLabel);
        summaryPanel.add(fromCurrencyCombo);
        summaryPanel.add(toCurrencyLabel);
        summaryPanel.add(toCurrencyCombo);
        summaryPanel.add(startDateLabel);
        summaryPanel.add(startDateField);
        summaryPanel.add(endDateLabel);
        summaryPanel.add(endDateField);


        JButton calculateButton = new JButton("Calculate Summary");

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        calculateButton.addActionListener(e -> {
            try {

                LocalDate startDate = LocalDate.parse(startDateField.getText(), formatter);
                LocalDate endDate = LocalDate.parse(endDateField.getText(), formatter);
                String fromCurrency = (String) fromCurrencyCombo.getSelectedItem();
                String toCurrency = (String) toCurrencyCombo.getSelectedItem();

                // use the rate history manager from the main instance and call summary method
                RateHistoryManager manager = Main.instance.rateHistoryManager;
                List<String> conversionRates = manager.getAllConversionRates(fromCurrency, toCurrency, startDate, endDate);

                Map<String, Double> statistics = manager.getSummaryStatistics(fromCurrency, toCurrency, startDate, endDate);
                StringBuilder result = new StringBuilder();
                if (conversionRates.isEmpty()) {
                    result.append("No conversion rates available for the selected range.\n");
                } else {
                    result.append("Conversion Rates from ").append(fromCurrency).append(" to ").append(toCurrency).append(":\n");
                    for (String rate : conversionRates) {
                        result.append(rate).append("\n");
                    }
                }

                if (statistics.isEmpty()) {
                    result.append("No data available for the selected range.");
                } else {
                    result.append("\nSummary Statistics:\n");
                    statistics.forEach((key, value) -> result.append(key).append(": ").append(String.format("%.2f", value)).append("\n"));
                }

                resultArea.setText(result.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        summaryPanel.add(calculateButton);
        add(summaryPanel, BorderLayout.NORTH);


        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // back button taken from admin panel
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.showCurrencyPanel();
        });
        add(backButton, BorderLayout.SOUTH);
    }
}
