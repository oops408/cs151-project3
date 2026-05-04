package persistence;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileStorageTest {
    @Test
    public void writeThenReadReturnsSameLines() throws Exception {
        String testFile = "test_storage_file.txt";
        Files.deleteIfExists(Path.of(testFile));

        FileStorage.writeAllLines(testFile, List.of("one", "two"));
        List<String> lines = FileStorage.readAllLines(testFile);

        assertEquals(List.of("one", "two"), lines);

        Files.deleteIfExists(Path.of(testFile));
    }

    @Test
    public void readingMissingFileCreatesEmptyFile() throws Exception {
        String testFile = "missing_test_storage_file.txt";
        Files.deleteIfExists(Path.of(testFile));

        List<String> lines = FileStorage.readAllLines(testFile);

        assertTrue(lines.isEmpty());
        assertTrue(Files.exists(Path.of(testFile)));

        Files.deleteIfExists(Path.of(testFile));
    }
}
