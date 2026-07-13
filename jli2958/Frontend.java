import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {

    private Scanner in;
    private BackendInterface backend;

    // stores the current collectables range for use in show commands
    private Integer low = null;
    private Integer high = null;

    public Frontend(Scanner in, BackendInterface backend) {
        this.in = in;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        showCommandInstructions();
        while (in.hasNextLine()) {
            System.out.print("\nEnter a command: ");
            String command = in.nextLine().trim();
            if (command.equals("quit")) {
                break;
            }
            // catch exceptions so one error doesn't end the loop
            try {
                processSingleCommand(command);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void showCommandInstructions() {
        System.out.println("Commands you can use:");
        System.out.println("  submit NAME CONTINENT SCORE DAMAGE_TAKEN COLLECTABLES COMPLETION_TIME");
        System.out.println("  submit multiple FILEPATH");
        System.out.println("  collectables MAX");
        System.out.println("  collectables MIN to MAX");
        System.out.println("  location CONTINENT");
        System.out.println("  show MAX_COUNT");
        System.out.println("  show fastest times");
        System.out.println("  help");
        System.out.println("  quit");
    }

    @Override
    public void processSingleCommand(String command) {
        command = command.trim();

        if (command.equals("help")) {
            showCommandInstructions();
        } else if (command.startsWith("submit multiple")) {
            submitMultiple(command);
        } else if (command.startsWith("submit")) {
            submit(command);
        } else if (command.startsWith("collectables")) {
            collectables(command);
        } else if (command.startsWith("location")) {
            location(command);
        } else if (command.equals("show fastest times")) {
            List<String> names = backend.getTopTen();
            printNames(names, names.size());
        } else if (command.startsWith("show")) {
            show(command);
        } else {
            System.out.println("Unknown command. Type help to see the options.");
        }
    }

    // submit NAME CONTINENT SCORE DAMAGE_TAKEN COLLECTABLES COMPLETION_TIME
    private void submit(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length != 7) {
            System.out.println("submit needs 6 arguments.");
            return;
        }
        GameRecord.Continent continent = toContinent(parts[2]);
        if (continent == null) {
            System.out.println(parts[2] + " is not a valid continent.");
            return;
        }
        Integer score = toInt(parts[3]);
        Integer damage = toInt(parts[4]);
        Integer collect = toInt(parts[5]);
        if (score == null || damage == null || collect == null) {
            System.out.println("score, damage taken and collectables must be numbers.");
            return;
        }
        backend.addRecord(new GameRecord(parts[1], continent, score, damage, collect, parts[6]));
        System.out.println("Added record for " + parts[1]);
    }

    // submit multiple FILEPATH
    private void submitMultiple(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length != 3) {
            System.out.println("submit multiple needs one file path.");
            return;
        }
        try {
            backend.readData(parts[2]);
            System.out.println("Loaded records from " + parts[2]);
        } catch (Exception e) {
            System.out.println("Could not read " + parts[2] + ": " + e.getMessage());
        }
    }

    // collectables MAX  or  collectables MIN to MAX
    private void collectables(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length == 2) {
            Integer max = toInt(parts[1]);
            if (max == null) {
                System.out.println("collectables MAX must be a number.");
                return;
            }
            low = null;
            high = max;
            backend.getAndSetRange(low, high);
        } else if (parts.length == 4 && parts[2].equals("to")) {
            Integer min = toInt(parts[1]);
            Integer max = toInt(parts[3]);
            if (min == null || max == null) {
                System.out.println("collectables MIN and MAX must be numbers.");
                return;
            }
            low = min;
            high = max;
            backend.getAndSetRange(low, high);
        } else {
            System.out.println("Use: collectables MAX  or  collectables MIN to MAX");
        }
    }

    // location CONTINENT
    private void location(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            System.out.println("location needs one continent.");
            return;
        }
        GameRecord.Continent continent = toContinent(parts[1]);
        if (continent == null) {
            System.out.println(parts[1] + " is not a valid continent.");
            return;
        }
        backend.applyAndSetFilter(continent);
    }

    // show MAX_COUNT
    private void show(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            System.out.println("Use: show MAX_COUNT  or  show fastest times");
            return;
        }
        Integer max = toInt(parts[1]);
        if (max == null || max < 0) {
            System.out.println("show needs a non-negative number (or use: show fastest times).");
            return;
        }
        List<String> names = backend.getAndSetRange(low, high);
        printNames(names, max);
    }

    // print up to limit names, one per line
    private void printNames(List<String> names, int limit) {
        if (names == null || names.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        int count = Math.min(limit, names.size());
        for (int i = 0; i < count; i++) {
            System.out.println(names.get(i));
        }
    }

    // returns null if the string doesn't match any continent
    private GameRecord.Continent toContinent(String s) {
        try {
            return GameRecord.Continent.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    // returns null if the string isn't a valid number
    private Integer toInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
