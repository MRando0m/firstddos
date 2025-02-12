public class FloatStats {
    private int count = 0;
    private double sum = 0;
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;

    public void addValue(double value) {
        count++;
        sum += value;
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    public int getCount() { return count; }
    public double getSum() { return sum; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getAverage() { return count == 0 ? 0 : sum / count; }
}