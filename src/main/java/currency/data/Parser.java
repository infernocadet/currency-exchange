package currency.data;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import currency.currency.Currency;


// all this class does is convert the .txt to json. the app will use the .json as its active source of currencies

public class Parser {
    private static final String TXT_FILE_PATH = "src/main/java/currency/data/staticCurrencies.txt";
    private static final String JSON_FILE_PATH = "src/main/java/currency/data/currencies.json";

    /**
     * all this does is read thru the txt file adn then writes content to json. json is used to load current exchange rate stuff
     */
    public void parseTxtToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode currenciesArray = objectMapper.createArrayNode(); // json array which holds currency objects

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
                    exchangeRatesNode.put(targetCurrency, rate);
                }
            }

            // writes to a json file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), currenciesArray);
            System.out.println("Wrote currencies from .txt to .json file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}