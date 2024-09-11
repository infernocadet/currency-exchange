package currency.currency;

import currency.Main;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Currency {

    private String name;
    private Map<String, Double> exchangeRates;
    private LocalDate lastUpdated;

    public Currency(String name, LocalDate lastUpdated) {
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.exchangeRates = new HashMap<String, Double>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static Currency getCurrencyByName(String name) {
        return Main.instance.currencies.stream()
                .filter(currency -> currency.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public String toString(){
        String misname = String.format("%s: ", this.name);
        return misname + exchangeRates.toString();
    }
}
