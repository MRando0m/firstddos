import java.io.*;

public class DataFilter {
    public void process(String[] files) {
        for (String file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.matches("\\d+")) {
                        // integers.txt
                    } else if (line.matches("\\d+\\.\\d+")) {
                        // floats.txt
                    } else {
                        // strings.txt
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + file);
            }
        }
    }
}
