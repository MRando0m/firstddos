import java.io.*;
import java.util.*;

public class DataFilter {
    private final ArgsParser argsParser;
    private final IntegerStats integerStats = new IntegerStats();
    private final FloatStats floatStats = new FloatStats();
    private final StringStats stringStats = new StringStats();
    private final Map<DataType, BufferedWriter> writers = new EnumMap<>(DataType.class);
    private final Set<DataType> failedTypes = new HashSet<>();

    public DataFilter(ArgsParser argsParser) {
        this.argsParser = argsParser;
    }

    public void processFiles() {
        for (String file : argsParser.getInputFiles()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading " + file + ": " + e.getMessage());
            }
        }
    }

    private void processLine(String line) {
        DataType type = determineDataType(line);
        updateStats(type, line);
        writeLine(type, line);
    }

    private DataType determineDataType(String line) {
        try {
            Integer.parseInt(line);
            return DataType.INTEGER;
        } catch (NumberFormatException e1) {
            try {
                Double.parseDouble(line);
                return DataType.FLOAT;
            } catch (NumberFormatException e2) {
                return DataType.STRING;
            }
        }
    }

    private void updateStats(DataType type, String line) {
        switch (type) {
            case INTEGER:
                integerStats.addValue(Integer.parseInt(line));
                break;
            case FLOAT:
                floatStats.addValue(Double.parseDouble(line));
                break;
            case STRING:
                stringStats.addValue(line);
                break;
        }
    }

    private void writeLine(DataType type, String line) {
        if (failedTypes.contains(type)) return;
        try {
            BufferedWriter writer = writers.get(type);
            if (writer == null) {
                String filename = argsParser.getPrefix() + type.name().toLowerCase() + "s.txt";
                File outputFile = new File(argsParser.getOutputDir(), filename);
                outputFile.getParentFile().mkdirs();
                boolean append = argsParser.isAppend() && outputFile.exists();
                writer = new BufferedWriter(new FileWriter(outputFile, append));
                writers.put(type, writer);
            }
            writer.write(line);
            writer.newLine();
        } catch (IOException | SecurityException e) {
            System.err.println("Error writing to " + type + " file: " + e.getMessage());
            failedTypes.add(type);
            closeWriter(type);
        }
    }

    public void printStatistics() {
        StatsType statsType = argsParser.getStatsType();
        if (statsType == StatsType.NONE) return;

        if (integerStats.getCount() > 0) {
            printIntegerStats(statsType);
        }
        if (floatStats.getCount() > 0) {
            printFloatStats(statsType);
        }
        if (stringStats.getCount() > 0) {
            printStringStats(statsType);
        }
    }

    private void printIntegerStats(StatsType statsType) {
        System.out.println("Integers:");
        System.out.println("  Count: " + integerStats.getCount());
        if (statsType == StatsType.FULL) {
            System.out.println("  Min: " + integerStats.getMin());
            System.out.println("  Max: " + integerStats.getMax());
            System.out.println("  Sum: " + integerStats.getSum());
            System.out.println("  Average: " + String.format("%.2f", integerStats.getAverage()));
        }
    }

    private void printFloatStats(StatsType statsType) {
        System.out.println("Floats:");
        System.out.println("  Count: " + floatStats.getCount());
        if (statsType == StatsType.FULL) {
            System.out.println("  Min: " + String.format("%.2f", floatStats.getMin()));
            System.out.println("  Max: " + String.format("%.2f", floatStats.getMax()));
            System.out.println("  Sum: " + String.format("%.2f", floatStats.getSum()));
            System.out.println("  Average: " + String.format("%.2f", floatStats.getAverage()));
        }
    }

    private void printStringStats(StatsType statsType) {
        System.out.println("Strings:");
        System.out.println("  Count: " + stringStats.getCount());
        if (statsType == StatsType.FULL) {
            System.out.println("  Shortest length: " + stringStats.getMinLength());
            System.out.println("  Longest length: " + stringStats.getMaxLength());
        }
    }

    public void closeWriters() {
        for (DataType type : DataType.values()) {
            closeWriter(type);
        }
    }

    private void closeWriter(DataType type) {
        try {
            BufferedWriter writer = writers.get(type);
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing " + type + " writer: " + e.getMessage());
        }
    }
}