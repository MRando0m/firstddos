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
        }
    }

}