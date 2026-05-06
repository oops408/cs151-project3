package persistence;

import utils.CryptoUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoUtilsTest {
    @Test
    void encryptThenDecryptReturnsOriginal() {
        String text = "hello world";
        String encrypted = CryptoUtils.encrypt(text);
        assertNotEquals(text, encrypted);
        assertEquals(text, CryptoUtils.decrypt(encrypted));
    }
}
