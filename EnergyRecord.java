
public class EnergyRecord {
    private String month;
    private double previousReading;
    private double currentReading;
    private double unitsUsed;
    private double estimatedBill;

    public EnergyRecord(String month, double previousReading,
                        double currentReading, double estimatedBill) {
        this.month = month;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
        this.unitsUsed = currentReading - previousReading;
        this.estimatedBill = estimatedBill;
    }

    public String getMonth() {
        return month;
    }

    public double getPreviousReading() {
        return previousReading;
    }

    public double getCurrentReading() {
        return currentReading;
    }

    public double getUnitsUsed() {
        return unitsUsed;
    }

    public double getEstimatedBill() {
        return estimatedBill;
    }

    public String toFileString() {
        return month + "|" + previousReading + "|"
                + currentReading + "|" + estimatedBill;
    }

    public static EnergyRecord fromFileString(String line) {
        String[] data = line.split("\\|", -1);

        if (data.length != 4) {
            return null;
        }

        try {
            String month = data[0];
            double previous = Double.parseDouble(data[1]);
            double current = Double.parseDouble(data[2]);
            double bill = Double.parseDouble(data[3]);

            if (!month.matches("\\d{4}-(0[1-9]|1[0-2])")
                    || !Double.isFinite(previous)
                    || !Double.isFinite(current)
                    || !Double.isFinite(bill)
                    || previous < 0
                    || current < previous
                    || bill < 0) {
                return null;
            }

            return new EnergyRecord(month, previous, current, bill);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
