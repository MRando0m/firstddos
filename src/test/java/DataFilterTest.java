import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataFilterTest {
    private static final Path TEST_DIR = Paths.get("fortest");
    private static final Path INPUT_DIR = TEST_DIR.resolve("input");
    private static final Path OUTPUT_DIR = TEST_DIR.resolve("output");

    @BeforeAll
    static void setup() throws Exception {
        // Создаем структуру директорий
        Files.createDirectories(INPUT_DIR);
        Files.createDirectories(OUTPUT_DIR);

        // Очищаем предыдущие результаты
        clearDirectory(OUTPUT_DIR);
    }

    @Test
    public void testDetermineDataType() {
        ArgsParser parser = new ArgsParser();
        DataFilter filter = new DataFilter(parser);

        assertEquals(DataType.INTEGER, filter.determineDataType("123"));
        assertEquals(DataType.FLOAT, filter.determineDataType("123.45"));
        assertEquals(DataType.STRING, filter.determineDataType("abc"));
    }

    @Test
    void testEdgeCasesDataTypes() {
        ArgsParser parser = new ArgsParser();
        DataFilter filter = new DataFilter(parser);

        assertEquals(DataType.INTEGER, filter.determineDataType("-123"));
        assertEquals(DataType.FLOAT, filter.determineDataType(".45"));
        assertEquals(DataType.STRING, filter.determineDataType("123a"));
    }

    @Test
    void testStatisticsCollection() throws Exception {
        // 1. Создаем тестовый входной файл
        Path inputFile = INPUT_DIR.resolve("test_input.txt");
        Files.write(inputFile, List.of("dummy content"));

        // 2. Настраиваем аргументы
        ArgsParser args = new ArgsParser();
        args.parse(new String[]{
                "-o", OUTPUT_DIR.toString(),
                "-p", "result_",
                inputFile.toString()
        });

        // 3. Инициализируем и запускаем фильтр
        DataFilter filter = new DataFilter(args);
        filter.processLine("10");
        filter.processLine("3.14");
        filter.processLine("text");

        // 4. Проверяем статистику
        assertEquals(1, filter.getIntegerStats().getCount());
        assertEquals(1, filter.getFloatStats().getCount());
        assertEquals(1, filter.getStringStats().getCount());

        // 5. Проверяем существование выходных файлов
        assertTrue(Files.exists(OUTPUT_DIR.resolve("result_integers.txt")));
        assertTrue(Files.exists(OUTPUT_DIR.resolve("result_floats.txt")));
        assertTrue(Files.exists(OUTPUT_DIR.resolve("result_strings.txt")));
    }

    private static void clearDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.delete(path); }
                        catch (IOException ignored) {}
                    });
        }
    }
}