import java.util.ArrayList;
import java.util.List;

public class ArgsParser {
    private String outputDir = "";
    private String prefix = "";
    private boolean append = false;
    private StatsType statsType = StatsType.NONE;
    private final List<String> inputFiles = new ArrayList<>();

    public void parse(String[] args) {
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-o":
                        if (i + 1 >= args.length) throw new IllegalArgumentException("-o requires a path");
                        outputDir = args[++i];
                        break;
                    case "-p":
                        if (i + 1 >= args.length) throw new IllegalArgumentException("-p requires a prefix");
                        prefix = args[++i];
                        break;
                    case "-a":
                        append = true;
                        break;
                    case "-s":
                        if (statsType == StatsType.FULL) throw new IllegalArgumentException("Both -s and -f specified");
                        statsType = StatsType.SHORT;
                        break;
                    case "-f":
                        if (statsType == StatsType.SHORT) throw new IllegalArgumentException("Both -s and -f specified");
                        statsType = StatsType.FULL;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown option: " + arg);
                }
                i++;
            } else {
                break;
            }
        }
        while (i < args.length) {
            inputFiles.add(args[i++]);
        }
        if (inputFiles.isEmpty()) throw new IllegalArgumentException("No input files");
    }

    // Getters
    public String getOutputDir() { return outputDir; }
    public String getPrefix() { return prefix; }
    public boolean isAppend() { return append; }
    public StatsType getStatsType() { return statsType; }
    public List<String> getInputFiles() { return inputFiles; }
}