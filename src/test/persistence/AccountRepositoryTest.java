package persistence;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountRepositoryTest {
    @Test
    public void createAccountAllowsLoginWithSamePassword() throws Exception {
        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));

        AccountRepository repository = new AccountRepository();
        boolean created = repository.createAccount("student", "password123");

        assertTrue(created);
        assertTrue(repository.validateLogin("student", "password123"));
        assertFalse(repository.validateLogin("student", "wrongPassword"));

        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));
    }

    @Test
    public void duplicateUsernameIsRejected() throws Exception {
        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));

        AccountRepository repository = new AccountRepository();
        assertTrue(repository.createAccount("student", "first"));
        assertFalse(repository.createAccount("student", "second"));

        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));
    }

    @Test
    public void passwordIsNotStoredInPlainText() throws Exception {
        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));

        AccountRepository repository = new AccountRepository();
        repository.createAccount("student", "secretPassword");
        List<String> lines = Files.readAllLines(Path.of(AccountRepository.FILE_NAME));

        assertFalse(lines.get(0).contains("secretPassword"));

        Files.deleteIfExists(Path.of(AccountRepository.FILE_NAME));
    }
}
