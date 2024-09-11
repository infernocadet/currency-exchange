package currency.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import currency.currency.Currency;


// this manages creating a csv file which tracks aa complete history of all changes in rate_history.csv
// rate_history.csv is used to track history of currency exchanges
// currencies.json is used to update the current rates
// currencies.json will be initially loaded using the Parser from the staticCurrencies.txt
// the application will pull all its data that the user can see (actively trade currencies with) from currencies.json
// rate_history.csv tracks all the changes and then we can look at those changes for the other stuff

public class RateHistoryManager {

    private String historyFilePath;
    private String jsonFilePath;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DEFAULT_HISTORY_FILE_PATH = "src/main/resources/rateHistory.csv";
    private static final String DEFAULT_JSON_FILE_PATH = "src/main/resources/currencies.json";


    public RateHistoryManager() {
        this(Paths.get("src/main/resources").toString());
    }


    public RateHistoryManager(String basePath) {
        this.historyFilePath = Paths.get(basePath, "rateHistory.csv").toString();
        this.jsonFilePath = Paths.get(basePath, "currencies.json").toString();

        File file = new File(historyFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("History CSV created: " + historyFilePath);

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

            try {
                JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

                for (JsonNode currencyNode : rootNode) {
                    String fromCurrency = currencyNode.get("code").asText();
                    JsonNode exchangeRates = currencyNode.get("exchangeRates");

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
            try (FileWriter writer = new FileWriter(historyFilePath, true)) {
                writer.append(String.format("%s,%s,%s,%.2f\n", date.format(DATE_FORMATTER), fromCurrency, toCurrency, rate));
                System.out.println("Rate history updated.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void appendRateHistory(Currency newCurrency) {
            LocalDate today = LocalDate.now();

            for (Map.Entry<String, Double> in : newCurrency.getExchangeRates().entrySet()) {
                String toCurrency = in.getKey();
                double rate = in.getValue();
                appendRateHistory(newCurrency.getName(), toCurrency, rate, today);

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

            try (BufferedReader br = new BufferedReader(new FileReader(historyFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    LocalDate entryDate = LocalDate.parse(parts[0], DATE_FORMATTER);
                    String entryFrom = parts[1];
                    String entryTo = parts[2];
                    double entryRate = Double.parseDouble(parts[3]);

                    if (entryFrom.equals(fromCurrency) && entryTo.equals(toCurrency)
                            && (entryDate.isEqual(startDate) || entryDate.isAfter(startDate))
                            && (entryDate.isEqual(endDate) || entryDate.isBefore(endDate))) {
                        historyRecords.add(parts);
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }

            return historyRecords;
        }

        public String compareLatestRates(String fromCurrency, String toCurrency) {
            List<String[]> historyRecords = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(historyFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    String entryFrom = parts[1];
                    String entryTo = parts[2];

                    if (entryFrom.equals(fromCurrency) && entryTo.equals(toCurrency)) {
                        historyRecords.add(parts);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (historyRecords.size() < 2) {
                return "";
            }

            historyRecords.sort((a, b) -> LocalDate.parse(b[0], DATE_FORMATTER).compareTo(LocalDate.parse(a[0], DATE_FORMATTER)));

            double latestRate = Double.parseDouble(historyRecords.get(0)[3]);
            double previousRate = Double.parseDouble(historyRecords.get(1)[3]);

            if (latestRate > previousRate) {
                return "(I)";
            } else if (latestRate < previousRate) {
                return "(D)";
            } else {
                return "";
            }
        }


        public Map<String, Double> getSummaryStatistics(String fromCurrency, String toCurrency, LocalDate startDate, LocalDate endDate){
            List<String[]> historyRecords = readRateHistory(fromCurrency, toCurrency, startDate, endDate);
            List<Double> rates = historyRecords.stream().map(record -> Double.parseDouble(record[3])).collect(Collectors.toList());

            if (rates.isEmpty()) {
                return Collections.emptyMap();
            }

            double average = rates.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double median = calculateMedian(rates);
            double max = rates.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double min = rates.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double stdDev = calculateStandardDeviation(rates, average);

            Map<String, Double> statistics = new HashMap<>();
            statistics.put("Average", average);
            statistics.put("Median", median);
            statistics.put("Maximum", max);
            statistics.put("Minimum", min);
            statistics.put("Standard Deviation", stdDev);

            return statistics;
        }

    private double calculateMedian(List<Double> rates) {
        Collections.sort(rates);
        int size = rates.size();
        if (size % 2 == 0){
            return(rates.get(size / 2 - 1) + rates.get(size / 2)) / 2.0;
        } else {
            return rates.get(size / 2);
        }
    }

    private double calculateStandardDeviation(List<Double> rates, double mean){
        double variance = rates.stream().mapToDouble(rate -> Math.pow(rate - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
}


