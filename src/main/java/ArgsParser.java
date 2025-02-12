import java.util.ArrayList;
import java.util.List;

public class ArgsParser {
    public String outputDir = "";
    public String prefix = "";
    public boolean append = false;
    public String statsType = "none";
    public List<String> files = new ArrayList<>();

    public void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o": outputDir = args[++i]; break;
                case "-p": prefix = args[++i]; break;
                case "-a": append = true; break;
                case "-s": statsType = "short"; break;
                case "-f": statsType = "full"; break;
                default: files.add(args[i]);
            }
        }
    }
}