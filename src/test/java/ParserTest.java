import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import currency.Main;
import currency.currency.Currency;
import currency.data.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Parser parser;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new Parser();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testParseTxtToJson() throws Exception {
        File txtFile = tempDir.resolve("testCurrencies.txt").toFile();
        try (FileWriter writer = new FileWriter(txtFile)) {
            writer.write("Currency: USD\n");
            writer.write("EUR: 0.85\n");
            writer.write("GBP: 0.75\n");
            writer.write("\n");
            writer.write("Currency: EUR\n");
            writer.write("USD: 1.18\n");
            writer.write("GBP: 0.88\n");
        }

        File jsonFile = tempDir.resolve("testCurrencies.json").toFile();
        parser.parseTxtToJson(txtFile.getAbsolutePath(), jsonFile.getAbsolutePath());
        assertTrue(jsonFile.exists());

        JsonNode root = objectMapper.readTree(jsonFile);
        assertEquals(2, root.size());

        JsonNode usd = root.get(0);
        assertEquals("USD", usd.get("code").asText());
        assertEquals(0.85, usd.get("exchangeRates").get("EUR").get("rate").asDouble());
        assertEquals(0.75, usd.get("exchangeRates").get("GBP").get("rate").asDouble());

        JsonNode eur = root.get(1);
        assertEquals("EUR", eur.get("code").asText());
        assertEquals(1.18, eur.get("exchangeRates").get("USD").get("rate").asDouble());
        assertEquals(0.88, eur.get("exchangeRates").get("GBP").get("rate").asDouble());
    }

    @Test
    void testUpdateJson() throws Exception {
        Main.instance = new Main();
        Currency usd = new Currency("USD");
        usd.getExchangeRates().put("EUR", 0.85);
        usd.getExchangeRates().put("GBP", 0.75);
        Currency eur = new Currency("EUR");
        eur.getExchangeRates().put("USD", 1.18);
        eur.getExchangeRates().put("GBP", 0.88);
        Main.instance.currencies = Arrays.asList(usd, eur);

        File jsonFile = tempDir.resolve("updatedCurrencies.json").toFile();

        parser.updateJson(jsonFile.getAbsolutePath());

        assertTrue(jsonFile.exists());

        JsonNode root = objectMapper.readTree(jsonFile);
        assertEquals(2, root.size());

        JsonNode updatedUsd = root.get(0);
        assertEquals("USD", updatedUsd.get("code").asText());
        assertEquals(0.85, updatedUsd.get("exchangeRates").get("EUR").get("rate").asDouble());
        assertEquals(0.75, updatedUsd.get("exchangeRates").get("GBP").get("rate").asDouble());
        assertEquals(LocalDate.now().toString(), updatedUsd.get("exchangeRates").get("EUR").get("lastUpdated").asText());

        JsonNode updatedEur = root.get(1);
        assertEquals("EUR", updatedEur.get("code").asText());
        assertEquals(1.18, updatedEur.get("exchangeRates").get("USD").get("rate").asDouble());
        assertEquals(0.88, updatedEur.get("exchangeRates").get("GBP").get("rate").asDouble());
        assertEquals(LocalDate.now().toString(), updatedEur.get("exchangeRates").get("USD").get("lastUpdated").asText());
    }

    @Test
    void testParseTxtToJsonWithInvalidFile() {
        String invalidFilePath = tempDir.resolve("non_existent_file.txt").toString();
        String jsonFilePath = tempDir.resolve("invalidTest.json").toString();
        assertDoesNotThrow(() -> parser.parseTxtToJson(invalidFilePath, jsonFilePath));
        assertFalse(new File(jsonFilePath).exists());
    }

    @Test
    void testUpdateJsonWithNoMainInstance() {
        Main.instance = null;
        String jsonFilePath = tempDir.resolve("noMainInstanceTest.json").toString();
        assertDoesNotThrow(() -> parser.updateJson(jsonFilePath));
        assertFalse(new File(jsonFilePath).exists());
    }
}