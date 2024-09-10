package currency.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import currency.currency.Currency;


// this manages creating a csv file which tracks aa complete history of all changes in rate_history.csv
// rate_history.csv is used to track history of currency exchanges
// currencies.json is used to update the current rates
// currencies.json will be initially loaded using the Parser from the staticCurrencies.txt
// the application will pull all its data that the user can see (actively trade currencies with) from currencies.json
// rate_history.csv tracks all the changes and then we can look at those changes for the other stuff

public class RateHistoryManager {

    private static final String HISTORY_FILE_PATH = "src/main/resources/rateHistory.csv";
    private static final String JSON_FILE_PATH = "src/main/resources/currencies.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // if there is no csv file (e.g. when app first runs
    public RateHistoryManager() {
        File file = new File(HISTORY_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("History CSV created: " + HISTORY_FILE_PATH);

                // then initialise rate history
                System.out.println("Initialising rate history");
                initialRateHistory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // read the newly loaded currencies.json
    public void initialRateHistory(){
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDate currentDate = LocalDate.now();

        // need to read current exchange rates from json and add this to rate history
        try {
            JsonNode rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));

            for (JsonNode currencyNode : rootNode) {
                String fromCurrency = currencyNode.get("code").asText();
                JsonNode exchangeRates = currencyNode.get("exchangeRates");

                // then go thru each target currency and the associated rate
                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = exchangeRates.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> rateEntry = fieldsIterator.next();
                    String toCurrency = rateEntry.getKey();
                    double rate = rateEntry.getValue().get("rate").asDouble();

                    appendRateHistory(fromCurrency, toCurrency, rate, currentDate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write a new currency exchange amendment
    // if the admin wants to update a rate, Main class should call RateHistoryManager.appendRateHistory() etc to record the change
    public void appendRateHistory(String fromCurrency, String toCurrency, double rate, LocalDate date) {

        try (FileWriter writer = new FileWriter(HISTORY_FILE_PATH, true)) {  // true means we will append
            writer.append(String.format("%s,%s,%s,%.2f\n", date.format(DATE_FORMATTER), fromCurrency, toCurrency, rate));
            System.out.println("Rate history updated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendRateHistory(Currency newCurrency) {
        LocalDate today = LocalDate.now();

        // iterate thru ex-rates and record
        for (Map.Entry<String, Double> in : newCurrency.getExchangeRates().entrySet()) {
            String toCurrency = in.getKey();
            double rate = in.getValue();
            appendRateHistory(newCurrency.getName(), toCurrency, rate, today);

            // add reciprocal
            Currency existing = Currency.getCurrencyByName(toCurrency);
            if (existing != null) {
                double reciprocal = 1 / rate;
                appendRateHistory(toCurrency, newCurrency.getName(), reciprocal, today);
            }
        }
    }

    // user wants to be able to print summary of conversion rates of 2 currencies they choose within a specific duration (start and end date)
    // wants to be able to get conversion rates, average, median, maximum, minimum and standard deviation of conversion rate

    public List<String[]> readRateHistory(String fromCurrency, String toCurrency, LocalDate startDate, LocalDate endDate) {
        List<String[]> historyRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) { // e.g. 'YYYY-MM-DD,AUD,USD,0.73'
                String[] parts = line.split(",");
                LocalDate entryDate = LocalDate.parse(parts[0], DATE_FORMATTER);
                String entryFrom = parts[1];
                String entryTo = parts[2];
                double entryRate = Double.parseDouble(parts[3]);

                if (entryFrom.equals(fromCurrency) && entryTo.equals(toCurrency)
                        && (entryDate.isEqual(startDate) || entryDate.isAfter(startDate))
                        && (entryDate.isEqual(endDate) || entryDate.isBefore(endDate))) {
                    historyRecords.add(parts); // list of multiple of these things: [[YYYY-MM-DD, AUD, USD, 0.73]]
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return historyRecords;
    }








}

// ask gpt this
