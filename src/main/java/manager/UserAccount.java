package manager;

public class UserAccount {
    private final String username;
    private final String encryptedPassword;

    public UserAccount(String username, String encryptedPassword) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
