
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.io.IOException;

/**
 * TeamTests contains opaque-box tests for teammate backend implementations.
 * These tests only use public BackendInterface methods and check returned
 * List<String> results.
 */
public class TeamTests {

    /**
     * Tests that readData() loads records and getTopTen() returns a usable,
     * non-empty list of String records after data has been loaded.
     */
    @Test
    public void testReadDataAndGetTopTenReturnsRecords() throws IOException {
        // Create backend through the public interface.
        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());

        // Load records before testing methods that depend on stored data.
        backend.readData("records.csv");

        // Call the public interface method.
        List<String> results = backend.getTopTen();

        // Check that a valid list is returned.
        assertNotNull(results, "getTopTen() should not return null after data is loaded.");

        // Check that the list contains at least one result.
        assertFalse(results.isEmpty(), "getTopTen() should return records after readData().");

        // getTopTen should not return more than ten results.
        assertTrue(results.size() <= 10, "getTopTen() should return at most ten records.");

        // Check that every returned record is usable String output.
        for (String record : results) {
            assertNotNull(record, "getTopTen() should not return null records.");
            assertFalse(record.trim().isEmpty(),
                "getTopTen() should not return empty String records.");
        }
    }

    /**
     * Tests that getAndSetRange() returns usable String records after data
     * has been loaded. The range is intentionally broad so that valid loaded
     * records should be included.
     */
    @Test
    public void testGetAndSetRangeAfterReadDataReturnsRecords() throws IOException {
        // Create backend through the public interface.
        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());

        // Load records before testing range behavior.
        backend.readData("records.csv");

        // Use a broad range so records from the loaded dataset should be included.
        List<String> results = backend.getAndSetRange(0, Integer.MAX_VALUE);

        // Check that the method returns a valid list.
        assertNotNull(results, "getAndSetRange() should not return null after data is loaded.");

        // Check that broad range search returns at least one record.
        assertFalse(results.isEmpty(),
            "A broad collectables range should return records after readData().");

        // Check that every returned record is usable String output.
        for (String record : results) {
            assertNotNull(record, "Range results should not contain null records.");
            assertFalse(record.trim().isEmpty(),
                "Range results should not contain empty String records.");
        }
    }

    /**
     * Tests that applyAndSetFilter() can be called after data is loaded and
     * returns a valid List<String>. This checks that the continent filter method
     * handles a valid public enum argument without depending on implementation
     * details.
     */
    @Test
    public void testApplyAndSetFilterAfterReadDataReturnsUsableList() throws IOException {
        // Create backend through the public interface.
        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());

        // Load records before testing filter behavior.
        backend.readData("records.csv");

        // Apply a valid continent filter through the public interface.
        List<String> results = backend.applyAndSetFilter(GameRecord.Continent.ASIA);

        // Check that the method returns a valid list, even if no Asia records exist.
        assertNotNull(results,
            "applyAndSetFilter() should return a non-null list for a valid continent.");

        // Check that every returned record is usable String output.
        for (String record : results) {
            assertNotNull(record, "Filtered results should not contain null records.");
            assertFalse(record.trim().isEmpty(),
                "Filtered results should not contain empty String records.");
        }
    }
}
