import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Scanner;

public class BackendTests {

    /**
     * Checks that addRecord inserts a record and that getAndSetRange can
     * retrieve records within a collectables range.
     */
    @Test
    public void roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        GameRecord record = new GameRecord("testPlayer",
                GameRecord.Continent.ASIA, 1000, 100, 125, "010:00:00");

        backend.addRecord(record);
        List<String> names = backend.getAndSetRange(120, 130);

        assertTrue(names.contains("testPlayer"));
    }

    /**
     * Checks that applyAndSetFilter filters records by continent.
     */
    @Test
    public void roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        List<String> names = backend.applyAndSetFilter(GameRecord.Continent.EUROPE);

        assertTrue(names.contains("speedRoyalty"));
        assertFalse(names.contains("xXxgamer47xXx"));
    }

    /**
     * Checks that readData can load records from the CSV file and that
     * getTopTen returns a list of matching record names.
     */
    @Test
    public void roleTest3() throws Exception {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        backend.readData("records.csv");
        List<String> topTen = backend.getTopTen();

        assertNotNull(topTen);
        assertTrue(topTen.size() > 0);
    }

    /**
     * Integration test that checks whether the real frontend can submit one
     * record into the real backend and tree, then retrieve it by collectables.
     */
    @Test
    public void submitAndCollectablesIntegrationTest() {
        // Simulate a user submitting one record, searching by collectables, then quitting.
        TextUITester tester = new TextUITester(
                "submit integrationAlpha ASIA 1000 100 77 001:00:00\n" +
                "collectables 70 to 80\n" +
                "quit\n");

        // Connect the real frontend to the real backend and real tree.
        IterableSortedCollection<GameRecord> tree = new IterableRedBlackTree<GameRecord>();
        BackendInterface backend = new Backend(tree);
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        // Run the full command loop with simulated user input.
        frontend.runCommandLoop();
        String output = tester.checkOutput();

        // The submitted record should be shown in the collectables search result.
        assertTrue(output.contains("integrationAlpha"));
    }

    /**
     * Integration test that checks whether the real frontend and backend can
     * apply a continent filter after records have been submitted.
     */
    @Test
    public void locationFilterIntegrationTest() {
        // Simulate submitting two records from different continents, then filtering.
        TextUITester tester = new TextUITester(
                "submit integrationAsia ASIA 900 100 90 002:00:00\n" +
                "submit integrationEurope EUROPE 950 100 95 003:00:00\n" +
                "location EUROPE\n" +
                "quit\n");

        // Use real integrated project components instead of placeholders.
        IterableSortedCollection<GameRecord> tree = new IterableRedBlackTree<GameRecord>();
        BackendInterface backend = new Backend(tree);
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        // Run commands through the partner frontend and this backend.
        frontend.runCommandLoop();
        String output = tester.checkOutput();

        // The European record should appear after filtering by EUROPE.
        assertTrue(output.contains("integrationEurope"));
    }

    /**
     * Integration test that checks whether the real frontend can ask the real
     * backend to show a limited number of records from the real tree.
     */
    @Test
    public void showMaxCountIntegrationTest() {
        // Simulate adding records and asking the frontend to show one result.
        TextUITester tester = new TextUITester(
                "submit integrationOne ASIA 1000 100 60 004:00:00\n" +
                "submit integrationTwo AFRICA 1100 100 120 005:00:00\n" +
                "show 1\n" +
                "quit\n");

        // Create the real integration chain.
        IterableSortedCollection<GameRecord> tree = new IterableRedBlackTree<GameRecord>();
        BackendInterface backend = new Backend(tree);
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        // Run the app through the frontend command loop.
        frontend.runCommandLoop();
        String output = tester.checkOutput();

        // At least one submitted record should be visible in the show command output.
        assertTrue(output.contains("integrationOne") || output.contains("integrationTwo"));
    }

    /**
     * Integration test that checks whether fastest-time results can be requested
     * through the real frontend after storing records in the real backend/tree.
     */
    @Test
    public void fastestTimesIntegrationTest() {
        // Simulate submitting records and asking for the fastest times.
        TextUITester tester = new TextUITester(
                "submit integrationSlow EUROPE 800 100 70 010:00:00\n" +
                "submit integrationFast EUROPE 850 100 80 001:00:00\n" +
                "show fastest times\n" +
                "quit\n");

        // Use real frontend, backend, and tree implementations.
        IterableSortedCollection<GameRecord> tree = new IterableRedBlackTree<GameRecord>();
        BackendInterface backend = new Backend(tree);
        FrontendInterface frontend = new Frontend(new Scanner(System.in), backend);

        // Run the full integrated app behavior.
        frontend.runCommandLoop();
        String output = tester.checkOutput();

        // The fastest submitted record should be included in the fastest-times output.
        assertTrue(output.contains("integrationFast"));
    }

}
