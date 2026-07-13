import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Scanner;

// tests for Frontend using the placeholder backend and tree
public class FrontendTests {

    // runCommandLoop shows instructions at start, help shows them again, quit exits
    @Test
    public void roleTest1() {
        TextUITester tester = new TextUITester("help\nquit\n");
        Scanner in = new Scanner(System.in);
        BackendInterface backend = new Backend_Placeholder(new Tree_Placeholder());
        Frontend frontend = new Frontend(in, backend);

        frontend.runCommandLoop();
        String output = tester.checkOutput();

        assertTrue(output.contains("submit"));
        assertTrue(output.contains("quit"));
        // shown once at startup, once for help -> should appear twice
        int first = output.indexOf("submit multiple");
        int second = output.indexOf("submit multiple", first + 1);
        assertTrue(first >= 0 && second > first);
    }

    // test submit and show commands, including invalid input handling
    @Test
    public void roleTest2() {
        Scanner in = new Scanner(System.in);
        Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

        // placeholder always inserts the ne0nVandal record on addRecord
        TextUITester t1 = new TextUITester("");
        frontend.processSingleCommand("submit ne0nVandal NORTH_AMERICA 49723 544 140 902:36:58");
        assertTrue(t1.checkOutput().contains("ne0nVandal"));

        TextUITester t2 = new TextUITester("");
        frontend.processSingleCommand("show 10");
        assertTrue(t2.checkOutput().contains("ne0nVandal"));

        // missing arguments should print an error, not throw
        TextUITester t3 = new TextUITester("");
        frontend.processSingleCommand("submit onlyName");
        assertTrue(t3.checkOutput().length() > 0);
    }

    // test submit multiple, location filter, and show fastest times
    @Test
    public void roleTest3() {
        Scanner in = new Scanner(System.in);
        Frontend frontend = new Frontend(in, new Backend_Placeholder(new Tree_Placeholder()));

        TextUITester t1 = new TextUITester("");
        frontend.processSingleCommand("submit multiple records.csv");
        assertTrue(t1.checkOutput().toLowerCase().contains("loaded"));

        // setting range/filter should not print any records
        TextUITester t2 = new TextUITester("");
        frontend.processSingleCommand("collectables 100 to 200");
        frontend.processSingleCommand("location EUROPE");
        assertFalse(t2.checkOutput().contains("speedRoyalty"));

        TextUITester t3 = new TextUITester("");
        frontend.processSingleCommand("show fastest times");
        assertTrue(t3.checkOutput().contains("speedRoyalty"));

        // invalid continent name should print an error
        TextUITester t4 = new TextUITester("");
        frontend.processSingleCommand("location ATLANTIS");
        assertTrue(t4.checkOutput().toLowerCase().contains("not a valid"));
    }
}
