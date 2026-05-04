package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileStorage {
    private FileStorage() { }

    public static List<String> readAllLines(String fileName) {
        try {
            Path path = Path.of(fileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read file: " + fileName, e);
        }
    }

    public static void writeAllLines(String fileName, List<String> lines) {
        try {
            Files.write(Path.of(fileName), new ArrayList<>(lines), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write file: " + fileName, e);
        }
    }
}
