package persistence;

import manager.UserAccount;
import utils.CryptoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository {
    public static final String FILE_NAME = "user_accounts.txt";

    public List<UserAccount> loadAccounts() {
        List<UserAccount> accounts = new ArrayList<>();
        for (String line : FileStorage.readAllLines(FILE_NAME)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\|", 2);
            if (parts.length == 2) {
                accounts.add(new UserAccount(parts[0], parts[1]));
            }
        }
        return accounts;
    }

    public boolean createAccount(String username, String rawPassword) {
        List<UserAccount> accounts = loadAccounts();
        Optional<UserAccount> existing = accounts.stream()
                .filter(account -> account.getUsername().equalsIgnoreCase(username))
                .findFirst();
        if (existing.isPresent()) {
            return false;
        }
        List<String> lines = new ArrayList<>(FileStorage.readAllLines(FILE_NAME));
        lines.add(username + "|" + CryptoUtils.encrypt(rawPassword));
        FileStorage.writeAllLines(FILE_NAME, lines);
        return true;
    }

    public boolean validateLogin(String username, String rawPassword) {
        return loadAccounts().stream().anyMatch(account ->
                account.getUsername().equalsIgnoreCase(username)
                        && CryptoUtils.decrypt(account.getEncryptedPassword()).equals(rawPassword));
    }
}
