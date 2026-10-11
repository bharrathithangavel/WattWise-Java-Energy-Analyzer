
import java.io.*;
import java.util.*;

public class WattWise {
    private static final ArrayList<EnergyRecord> history =
            new ArrayList<>();

    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "wattwise_history.txt";

    // Illustrative rates only; replace with your applicable tariff.
    private static final double RATE_1 = 1.50;
    private static final double RATE_2 = 2.50;
    private static final double RATE_3 = 4.00;
    private static final double RATE_4 = 6.00;

    public static void main(String[] args) {
        loadHistory();

        System.out.println("======================================");
        System.out.println("       WATTWISE ENERGY ANALYZER");
        System.out.println("======================================");

        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addReading();
                    break;
                case 2:
                    viewHistory();
                    break;
                case 3:
                    compareLatestMonths();
                    break;
                case 4:
                    showUsageAlert();
                    break;
                case 5:
                    showSavingTips();
                    break;
                case 6:
                    running = false;
                    System.out.println("Thank you for using WattWise!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n------------- MAIN MENU -------------");
        System.out.println("1. Add Monthly Meter Reading");
        System.out.println("2. View Electricity History");
        System.out.println("3. Compare Latest Months");
        System.out.println("4. Check Unusual Usage Alert");
        System.out.println("5. Get Electricity Saving Tips");
        System.out.println("6. Exit");
        System.out.println("-------------------------------------");
    }

    private static void addReading() {
        System.out.println("\n--- Add Monthly Reading ---");

        String month = readMonth();

        if (findMonth(month) != null) {
            System.out.println("This month is already recorded.");
            return;
        }

        double previous = readNonNegative(
                "Enter previous meter reading: ");

        double current = readNonNegative(
                "Enter current meter reading: ");

        if (current < previous) {
            System.out.println(
                    "Current reading cannot be below previous reading.");
            return;
        }

        double units = current - previous;
        double bill = calculateBill(units);

        EnergyRecord record =
                new EnergyRecord(month, previous, current, bill);

        history.add(record);
        sortHistory();

        if (saveHistory()) {
            System.out.println("\nReading saved successfully!");
            displayRecord(record);
            giveImmediateFeedback(record);
        } else {
            history.remove(record);
            System.out.println(
                    "Could not save the record. Please try again.");
        }
    }

    // Progressive slab calculation using sample rates.
    private static double calculateBill(double units) {
        double bill = 0;

        double slab1 = Math.min(units, 100);
        bill += slab1 * RATE_1;

        if (units > 100) {
            double slab2 = Math.min(units - 100, 100);
            bill += slab2 * RATE_2;
        }

        if (units > 200) {
            double slab3 = Math.min(units - 200, 300);
            bill += slab3 * RATE_3;
        }

        if (units > 500) {
            double slab4 = units - 500;
            bill += slab4 * RATE_4;
        }

        return bill;
    }

    private static void displayRecord(EnergyRecord record) {
        System.out.println("\n--------- MONTHLY REPORT ---------");
        System.out.println("Month: " + record.getMonth());
        System.out.printf("Previous reading: %.2f%n",
                record.getPreviousReading());
        System.out.printf("Current reading: %.2f%n",
                record.getCurrentReading());
        System.out.printf("Units consumed: %.2f kWh%n",
                record.getUnitsUsed());
        System.out.printf("Estimated bill: Rs. %.2f%n",
                record.getEstimatedBill());
        System.out.println("----------------------------------");
    }

    private static void viewHistory() {
        System.out.println("\n--- Electricity Usage History ---");

        if (history.isEmpty()) {
            System.out.println("No readings recorded yet.");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-12s %-12s%n",
                "Month", "Previous", "Current", "Units", "Bill (Rs.)");

        System.out.println(
                "---------------------------------------------------------------");

        double totalUnits = 0;
        double totalBill = 0;

        for (EnergyRecord record : history) {
            System.out.printf("%-10s %-12.2f %-12.2f %-12.2f %-12.2f%n",
                    record.getMonth(),
                    record.getPreviousReading(),
                    record.getCurrentReading(),
                    record.getUnitsUsed(),
                    record.getEstimatedBill());

            totalUnits += record.getUnitsUsed();
            totalBill += record.getEstimatedBill();
        }

        System.out.println(
                "---------------------------------------------------------------");
        System.out.printf("Total units recorded: %.2f kWh%n", totalUnits);
        System.out.printf("Total estimated bills: Rs. %.2f%n", totalBill);
    }

    private static void compareLatestMonths() {
        System.out.println("\n--- Month-to-Month Comparison ---");

        if (history.size() < 2) {
            System.out.println(
                    "Record at least two different months to compare.");
            return;
        }

        EnergyRecord latest = history.get(history.size() - 1);
        EnergyRecord previous = history.get(history.size() - 2);

        double difference =
                latest.getUnitsUsed() - previous.getUnitsUsed();

        System.out.println("Previous month: " + previous.getMonth());
        System.out.println("Latest month: " + latest.getMonth());

        System.out.printf("Previous usage: %.2f kWh%n",
                previous.getUnitsUsed());
        System.out.printf("Latest usage: %.2f kWh%n",
                latest.getUnitsUsed());

        if (difference > 0) {
            System.out.printf("Usage increased by %.2f kWh.%n",
                    difference);
        } else if (difference < 0) {
            System.out.printf("Usage decreased by %.2f kWh.%n",
                    Math.abs(difference));
        } else {
            System.out.println("Usage remained unchanged.");
        }

        if (previous.getUnitsUsed() > 0) {
            double percentage =
                    difference / previous.getUnitsUsed() * 100;

            System.out.printf("Usage change: %.2f%%%n", percentage);
        } else if (latest.getUnitsUsed() > 0) {
            System.out.println(
                    "Percentage change is undefined because previous usage was zero.");
        } else {
            System.out.println("Both months have zero usage.");
        }

        double billDifference =
                latest.getEstimatedBill() - previous.getEstimatedBill();

        System.out.printf("Estimated bill change: Rs. %.2f%n",
                billDifference);
    }

    private static void showUsageAlert() {
        System.out.println("\n--- Unusual Usage Alert ---");

        if (history.isEmpty()) {
            System.out.println("Add a reading to check usage.");
            return;
        }

        EnergyRecord latest = history.get(history.size() - 1);
        System.out.printf("Latest consumption: %.2f kWh%n",
                latest.getUnitsUsed());

        if (latest.getUnitsUsed() > 500) {
            System.out.println(
                    "HIGH USAGE: Review your appliance consumption.");
        } else if (latest.getUnitsUsed() > 300) {
            System.out.println(
                    "MODERATE ALERT: Your usage is relatively high.");
        } else {
            System.out.println(
                    "No high-usage threshold alert for this month.");
        }

        if (history.size() >= 2) {
            EnergyRecord previous =
                    history.get(history.size() - 2);

            if (previous.getUnitsUsed() > 0) {
                double increase =
                        (latest.getUnitsUsed()
                        - previous.getUnitsUsed())
                        / previous.getUnitsUsed() * 100;

                if (increase >= 25) {
                    System.out.printf(
                            "MONTHLY ALERT: Consumption increased by %.2f%%.%n",
                            increase);
                }
            } else if (latest.getUnitsUsed() > 0) {
                System.out.println(
                        "MONTHLY ALERT: Usage increased from zero.");
            }
        }
    }

    private static void showSavingTips() {
        System.out.println("\n--- Personalized Saving Suggestions ---");

        if (history.isEmpty()) {
            System.out.println(
                    "Add a meter reading to receive relevant suggestions.");
            return;
        }

        EnergyRecord latest = history.get(history.size() - 1);
        double units = latest.getUnitsUsed();

        if (units > 500) {
            System.out.println(
                    "1. Review high-power appliances and AC usage.");
            System.out.println(
                    "2. Check for appliances left running unnecessarily.");
            System.out.println(
                    "3. Compare your daily usage across the month.");
        } else if (units > 300) {
            System.out.println(
                    "1. Switch off lights and fans when not needed.");
            System.out.println(
                    "2. Use energy-efficient lighting.");
            System.out.println(
                    "3. Avoid unnecessary standby power.");
        } else {
            System.out.println(
                    "1. Continue monitoring your monthly readings.");
            System.out.println(
                    "2. Prefer efficient appliances when replacing old ones.");
            System.out.println(
                    "3. Keep a record to identify future usage increases.");
        }

        System.out.println(
                "These are general tips, not a guaranteed savings estimate.");
    }

    private static void giveImmediateFeedback(EnergyRecord record) {
        if (record.getUnitsUsed() > 500) {
            System.out.println(
                    "Tip: Your recorded usage is high; review appliance use.");
        } else if (record.getUnitsUsed() > 300) {
            System.out.println(
                    "Tip: Compare this month with earlier readings.");
        } else {
            System.out.println(
                    "Tip: Keep recording monthly readings to track trends.");
        }
    }

    private static EnergyRecord findMonth(String month) {
        for (EnergyRecord record : history) {
            if (record.getMonth().equals(month)) {
                return record;
            }
        }
        return null;
    }

    private static void sortHistory() {
        Collections.sort(history, new Comparator<EnergyRecord>() {
            @Override
            public int compare(EnergyRecord a, EnergyRecord b) {
                return a.getMonth().compareTo(b.getMonth());
            }
        });
    }

    private static String readMonth() {
        while (true) {
            System.out.print("Enter month (YYYY-MM): ");
            String month = scanner.nextLine().trim();

            if (month.matches("\\d{4}-(0[1-9]|1[0-2])")) {
                return month;
            }

            System.out.println(
                    "Invalid month. Example: 2026-10");
        }
    }

    private static double readNonNegative(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }

                System.out.println(
                        "Enter a finite, non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static void loadHistory() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Starting with an empty history.");
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                EnergyRecord record =
                        EnergyRecord.fromFileString(line);

                if (record != null && findMonth(record.getMonth()) == null) {
                    history.add(record);
                }
            }

            sortHistory();
            System.out.println(
                    history.size() + " saved month(s) loaded.");

        } catch (IOException e) {
            System.out.println(
                    "Could not load history: " + e.getMessage());
        }
    }

    private static boolean saveHistory() {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (EnergyRecord record : history) {
                writer.write(record.toFileString());
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            System.out.println(
                    "Could not save history: " + e.getMessage());
            return false;
        }
    }
}
