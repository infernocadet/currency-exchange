package currency.data;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import currency.Main;
import currency.currency.Currency;


// all this class does is convert the .txt to json. the app will use the .json as its active source of currencies

public class Parser {
    private static final String DEFAULT_TXT_FILE_PATH = "src/main/resources/staticCurrencies.txt";
    private static final String DEFAULT_JSON_FILE_PATH = "src/main/resources/currencies.json";

    public void parseTxtToJson() {
        parseTxtToJson(DEFAULT_TXT_FILE_PATH, DEFAULT_JSON_FILE_PATH);
    }

    /**
     * all this does is read thru the txt file adn then writes content to json. json is used to load current exchange rate stuff
     */
    public void parseTxtToJson(String TXT_FILE_PATH, String JSON_FILE_PATH) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode currenciesArray = objectMapper.createArrayNode(); // json array which holds currency objects
        String currentDate = LocalDate.now().toString(); // current date in 'yyyy-mm-dd'

        try (BufferedReader br = new BufferedReader(new FileReader(TXT_FILE_PATH))) {
            String line;
            ObjectNode currentCurrencyNode = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue; // for empty lines
                }

                if (line.startsWith("Currency:")) {

                    // make new currency object
                    String currencyName = line.split(":")[1].trim();
                    currentCurrencyNode = objectMapper.createObjectNode(); // new json object for specific currency
                    currentCurrencyNode.put("code", currencyName);
                    currentCurrencyNode.putObject("exchangeRates"); // another like list of diff rates for the current rate. this is empty atm
                    currenciesArray.add(currentCurrencyNode); // then we add the currency to the json array
                } else if (currentCurrencyNode != null) {

                    // this part looks at the values of the ex rates for a specific currency
                    String[] parts = line.split(":");
                    String targetCurrency = parts[0].trim();
                    double rate = Double.parseDouble(parts[1].trim());

                    // then add exchange rate to the current currencies exchangeRate object
                    ObjectNode exchangeRatesNode = (ObjectNode) currentCurrencyNode.get("exchangeRates");
                    ObjectNode rateNode = objectMapper.createObjectNode();
                    rateNode.put("rate", rate);
                    rateNode.put("lastUpdated", currentDate);
                    exchangeRatesNode.put(targetCurrency, rateNode);
                }
            }

            // writes to a json file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), currenciesArray);
            System.out.println("Wrote currencies from .txt to .json file.");
        } catch (IOException e) {
            System.out.println("Error parsing .txt to .json: " + e.getMessage());
        }
    }

    public void updateJson() {
        updateJson(DEFAULT_JSON_FILE_PATH);
    }

    public void updateJson(String JSON_FILE_PATH) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode currenciesArray = objectMapper.createArrayNode();
            if (Main.instance != null && Main.instance.currencies != null) {
                for (Currency currency : Main.instance.currencies) {
                    ObjectNode currencyNode = new ObjectMapper().createObjectNode();
                    currencyNode.put("code", currency.getName());
                    ObjectNode exchangeRatesNode = new ObjectMapper().createObjectNode();
                    for (Map.Entry<String, Double> entry : currency.getExchangeRates().entrySet()) {
                        ObjectNode rateNode = new ObjectMapper().createObjectNode();
                        rateNode.put("rate", entry.getValue());
                        rateNode.put("lastUpdated", LocalDate.now().toString());
                        exchangeRatesNode.put(entry.getKey(), rateNode);
                    }
                    currencyNode.set("exchangeRates", exchangeRatesNode);
                    currenciesArray.add(currencyNode);
                }
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), currenciesArray);
            } else {
                System.out.println("Error: Main.instance or currencies is null");
            }
        } catch (Exception e) {
            System.out.println("Error updating JSON: " + e.getMessage());
        }
    }
}
