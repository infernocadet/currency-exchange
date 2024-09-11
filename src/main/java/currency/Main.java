package currency;

import currency.currency.Currency;
import currency.data.Parser;
import currency.data.RateHistoryManager;
import currency.gui.Frame;
import currency.user.Role;
import currency.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Main instance;
    public User loggedInUser;
    public List<User> users = new ArrayList<>();
    public List<Currency> currencies = new ArrayList<>();
    public List<Currency> activeCurrencies = new ArrayList<>(); // will store the four main currencies to display
    private static final String JSON_FILE_PATH = "src/main/resources/currencies.json";
    public RateHistoryManager rateHistoryManager;

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void loadUsers() {
        String userDatabase = "src/main/resources/users.json";
        JSONParser parser = new JSONParser();
        try {
             JSONArray userArray = (JSONArray) parser.parse(new FileReader(userDatabase));
             for (Object userObject : userArray) {
                 JSONObject userJSON = (JSONObject) userObject;
                 String username = (String) userJSON.get("username");
                 String password = (String) userJSON.get("password");
                 String role = (String) userJSON.get("role");
                 users.add(new User(username, password, Role.valueOf(role)));
             }
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    public void loadCurrencies() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray currencyArray = (JSONArray) parser.parse(new FileReader(JSON_FILE_PATH));
            for (Object currencyObject : currencyArray) {
                JSONObject currencyJSON = (JSONObject) currencyObject;
                String code = (String) currencyJSON.get("code");
                JSONObject exchangeRates = (JSONObject) currencyJSON.get("exchangeRates");
                Currency currency = new Currency(code, LocalDate.now());
                for (Object rateObject : exchangeRates.keySet()) {
                    String targetCurrency = (String) rateObject;
                    JSONObject rate = (JSONObject) exchangeRates.get(targetCurrency);
                    currency.setLastUpdated(LocalDate.parse(rate.get("lastUpdated").toString()));
                    currency.getExchangeRates().put(targetCurrency, (double) rate.get("rate"));
                }
                currencies.add(currency);
            }

            // after we load all currencies into memory, then we need to just select 4 currencies to put in the activeCurrencies
            // when initial load, it'll be the first four. this can be changed later
            for (int i = 0; i < 4; i++){
                activeCurrencies.add(currencies.get(i));
            }

        } catch (Exception e) {
            System.out.println("Error loading currencies: " + e.getMessage());
        }
    }

    public void initialLoad() {
        loadUsers();
        File jsonFile = new File(JSON_FILE_PATH);
        if (jsonFile.exists()) {
            System.out.println("Loading currencies from existing JSON...");
            loadCurrencies();
            System.out.println("Currencies loaded from JSON file.");
        } else {
            System.out.println("JSON File not found - creating new JSON...");
            new Parser().parseTxtToJson();
            loadCurrencies();
            System.out.println("Currencies created and loaded from new JSON file.");
        }
    }

    public Main() {
        instance = this;
        initialLoad();
        this.rateHistoryManager = new RateHistoryManager();
        new Frame();
    }

    public static void main(String[] args) {
        new Main();
    }
}