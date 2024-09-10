import currency.Main;
import currency.currency.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    private Currency usd;
    private Currency eur;

    @BeforeEach
    void setUp() {
        usd = new Currency("USD");
        eur = new Currency("EUR");

        usd.getExchangeRates().put("EUR", 0.85);
        eur.getExchangeRates().put("USD", 1.18);

        Main.instance = new Main();
        Main.instance.currencies = new ArrayList<>();
        Main.instance.currencies.add(usd);
        Main.instance.currencies.add(eur);
    }

    @Test
    void testConstructor() {
        Currency currency = new Currency("GBP");
        assertEquals("GBP", currency.getName());
        assertTrue(currency.getExchangeRates().isEmpty());
    }

    @Test
    void testGetName() {
        assertEquals("USD", usd.getName());
        assertEquals("EUR", eur.getName());
    }

    @Test
    void testGetExchangeRates() {
        Map<String, Double> usdRates = usd.getExchangeRates();
        Map<String, Double> eurRates = eur.getExchangeRates();

        assertEquals(0.85, usdRates.get("EUR"));
        assertEquals(1.18, eurRates.get("USD"));
    }

    @Test
    void testGetCurrencyByName() {
        Currency foundUSD = Currency.getCurrencyByName("USD");
        Currency foundEUR = Currency.getCurrencyByName("EUR");
        Currency notFound = Currency.getCurrencyByName("GBP");

        assertNotNull(foundUSD);
        assertEquals("USD", foundUSD.getName());
        assertNotNull(foundEUR);
        assertEquals("EUR", foundEUR.getName());
        assertNull(notFound);
    }

    @Test
    void testGetCurrencyByNameCaseInsensitive() {
        Currency foundUSD = Currency.getCurrencyByName("usd");
        Currency foundEUR = Currency.getCurrencyByName("eUr");

        assertNotNull(foundUSD);
        assertEquals("USD", foundUSD.getName());
        assertNotNull(foundEUR);
        assertEquals("EUR", foundEUR.getName());
    }

    @Test
    void testGetCurrencyByNameEmptyString() {
        Currency notFound = Currency.getCurrencyByName("");
        assertNull(notFound);
    }

    @Test
    void testGetCurrencyByNameNullInput() {
        Currency notFound = Currency.getCurrencyByName(null);
        assertNull(notFound);
    }
}