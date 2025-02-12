public class StringStats {
    private int count = 0;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    public void addValue(String value) {
        count++;
        int length = value.length();
        minLength = Math.min(minLength, length);
        maxLength = Math.max(maxLength, length);
    }

    public int getCount() { return count; }
    public int getMinLength() { return minLength; }
    public int getMaxLength() { return maxLength; }
}
