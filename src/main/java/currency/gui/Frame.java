package currency.gui;

import currency.currency.Currency;

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
        setSize(750, 350);
        currentPanel = new CurrencyPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showAdminPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(600, 300);
        currentPanel = new AdminPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showSummaryPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(500, 300);
        currentPanel = new SummaryPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showConfigureCurrencyPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(500, 300);
        currentPanel = new ConfigureCurrencyPanel(this);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showExchangeRatePanel(Currency currency) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        setSize(500, 300);
        currentPanel = new ExchangeRatePanel(this, currency);
        add(currentPanel);
        revalidate();
        repaint();
    }
}
