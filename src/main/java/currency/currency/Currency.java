package currency.currency;

import currency.Main;

import java.util.HashMap;
import java.util.Map;

public class Currency {

    private String name;
    private Map<String, Double> exchangeRates;

    public Currency(String name) {
        this.name = name;
        this.exchangeRates = new HashMap<String, Double>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }

    public static Currency getCurrencyByName(String name) {
        return Main.instance.currencies.stream()
                .filter(currency -> currency.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
