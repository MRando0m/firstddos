public class Main {
    public static void main(String[] args) {
        ArgsParser parser = new ArgsParser();
        try {
            parser.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        DataFilter filter = new DataFilter(parser);
        try {
            filter.processFiles();
            filter.printStatistics();
        } catch (Exception e) {
            System.err.println("Processing error: " + e.getMessage());
        } finally {
            filter.closeWriters();
        }
    }
}