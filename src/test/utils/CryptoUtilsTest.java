package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoUtilsTest {
    @Test
    void encryptThenDecryptReturnsOriginalText() {
        String original = "HumanPlayer,950,50,AH|10S";

        String encrypted = CryptoUtils.encrypt(original);
        String decrypted = CryptoUtils.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    void encryptedTextDoesNotExposePlainText() {
        String original = "password123 QueenHeart 8S";

        String encrypted = CryptoUtils.encrypt(original);

        assertNotEquals(original, encrypted);
        assertFalse(encrypted.contains("password123"));
        assertFalse(encrypted.contains("QueenHeart"));
    }
}
