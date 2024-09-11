import currency.data.RateHistoryManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

class RateHistoryManagerTest {

    private RateHistoryManager manager;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        Files.createFile(tempDir.resolve("rateHistory.csv"));
        Files.createFile(tempDir.resolve("currencies.json"));

        manager = new RateHistoryManager(tempDir.toString());
    }

    @Test
    void testAppendRateHistory() throws Exception {
        LocalDate date = LocalDate.of(2024, 9, 11);
        manager.appendRateHistory("USD", "EUR", 0.85, date);

        List<String[]> history = manager.readRateHistory("USD", "EUR", date, date);
        assertFalse(history.isEmpty());
        assertEquals("2024-09-11,USD,EUR,0.85", String.join(",", history.get(0)));
    }

    @Test
    void testReadRateHistory() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 9, 10);
        LocalDate endDate = LocalDate.of(2024, 9, 12);

        manager.appendRateHistory("USD", "EUR", 0.85, startDate);
        manager.appendRateHistory("USD", "EUR", 0.86, endDate);

        List<String[]> history = manager.readRateHistory("USD", "EUR", startDate, endDate);
        assertEquals(2, history.size());
    }

    @Test
    void testGetSummaryStatistics() throws Exception {
        LocalDate date1 = LocalDate.of(2024, 9, 10);
        LocalDate date2 = LocalDate.of(2024, 9, 11);

        manager.appendRateHistory("USD", "EUR", 0.85, date1);
        manager.appendRateHistory("USD", "EUR", 0.87, date2);

        Map<String, Double> stats = manager.getSummaryStatistics("USD", "EUR", date1, date2);

        assertEquals(0.86, stats.get("Average"), 0.001);
        assertEquals(0.87, stats.get("Maximum"), 0.001);
        assertEquals(0.85, stats.get("Minimum"), 0.001);
    }

    @Test
    void testCompareLatestRates() throws Exception {
        LocalDate date1 = LocalDate.of(2024, 9, 10);
        LocalDate date2 = LocalDate.of(2024, 9, 11);

        manager.appendRateHistory("USD", "EUR", 0.85, date1);
        manager.appendRateHistory("USD", "EUR", 0.87, date2);

        assertEquals("(I)", manager.compareLatestRates("USD", "EUR"));
    }

    @Test
    void testInitialRateHistory() throws Exception {
        List<String[]> history = manager.readRateHistory("USD", "EUR", LocalDate.now(), LocalDate.now());
        assertFalse(history.isEmpty());
    }
}