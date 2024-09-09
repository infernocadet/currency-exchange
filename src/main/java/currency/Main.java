package currency;

import currency.currency.Currency;
import currency.data.Parser;
import currency.gui.Frame;
import currency.user.Role;
import currency.user.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Main instance;
    public User loggedInUser;
    public List<User> users = new ArrayList<>();
    public List<Currency> currencies = new ArrayList<>();
    private static final String JSON_FILE_PATH = "src/main/java/currency/data/currencies.json";

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

    public void initialLoad() {
        File jsonFile = new File(JSON_FILE_PATH);

        if (!jsonFile.exists()) {
            System.out.println("Loading currencies from existing JSON...");
        } else {
            System.out.println("JSON File not found - making new JSON...");
            Parser parser = new Parser();
            parser.parseTxtToJson();
        }
//        List<Currency> currencies = loadCurrencies();

    }

    public void loadCurrencies() {

        // TODO: Load actual currencies
        Currency aud = new Currency("AUD");
        aud.getExchangeRates().put("SGD", 0.87);
        aud.getExchangeRates().put("USD", 0.67);
        aud.getExchangeRates().put("EUR", 0.60);
        currencies.add(aud);
        Currency sgd = new Currency("SGD");
        sgd.getExchangeRates().put("AUD", 1.15);
        sgd.getExchangeRates().put("USD", 0.77);
        sgd.getExchangeRates().put("EUR", 0.69);
        currencies.add(sgd);
        Currency usd = new Currency("USD");
        usd.getExchangeRates().put("AUD", 1.50);
        usd.getExchangeRates().put("SGD", 1.30);
        usd.getExchangeRates().put("EUR", 0.90);
        currencies.add(usd);
        Currency eur = new Currency("EUR");
        eur.getExchangeRates().put("AUD", 1.66);
        eur.getExchangeRates().put("SGD", 1.44);
        eur.getExchangeRates().put("USD", 1.11);
        currencies.add(eur);
    }

    public Main() {
        instance = this;
        loadUsers();
        loadCurrencies();
        initialLoad();
        new Frame();
    }

    public static void main(String[] args) {
        new Main();
    }
}