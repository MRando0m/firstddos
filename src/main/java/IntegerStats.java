public class IntegerStats {
    private int count = 0;
    private long sum = 0;
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public void addValue(int value) {
        count++;
        sum += value;
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    public int getCount() { return count; }
    public long getSum() { return sum; }
    public int getMin() { return min; }
    public int getMax() { return max; }
    public double getAverage() { return count == 0 ? 0 : (double) sum / count; }
}