import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArgsParserTest {
    @Test
    void testValidOptionsAndFiles() {
        ArgsParser parser = new ArgsParser();
        parser.parse(new String[]{"-o", "out", "-p", "res_", "file1.txt", "file2.txt"});

        assertEquals("out", parser.getOutputDir());
        assertEquals("res_", parser.getPrefix());
        assertEquals(2, parser.getInputFiles().size());
    }

    @Test
    void testConflictingStatsOptions() {
        ArgsParser parser = new ArgsParser();

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(new String[]{"-s", "-f", "file.txt"});
        });
    }

    @Test
    void testMissingOptionValue() {
        ArgsParser parser = new ArgsParser();

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(new String[]{"-o"});
        });
    }
}