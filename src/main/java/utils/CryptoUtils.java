package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "CS151Spring26Key".getBytes(StandardCharsets.UTF_8);

    private CryptoUtils() { }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to encrypt text", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decrypt text", e);
        }
    }
}

/* Original:
package utils;

import javax.crypto.Cipher;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class EncryptionUtil {
    private static SecretKey key = new SecretKeySpec("1234567890123456".getBytes(), "AES");

    public static String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
    }

    public static String decrypt (String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
    }
}
*/
