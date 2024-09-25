package com.dmsc.libraryserviceapi.service.hashing;

import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import lombok.SneakyThrows;
import org.springframework.util.MultiValueMap;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * This class will be responsible to implement AES to hash an identifier.
 * Recurring to autoconfigure and properties this implementation can be configured to via properties by having multiple implementations
 */
public class AESIdentifierHashImpl implements IdentifierHashService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private final Cipher cipher;
    private final SecretKey secretKey;

    public AESIdentifierHashImpl(String hashKey) {
        GenerateSecretKey generateSecretKey = key -> {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        };
        this.secretKey = generateSecretKey.apply(hashKey);
        this.cipher = getCipher();
    }

    private Cipher getCipher() {
        try {
            return Cipher.getInstance(TRANSFORMATION); //NOSONAR this will be used to hash the id
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String hash(BookSystemEnum bookSystemEnum, String id) {
        return encrypt(IdGeneratorUtil.build(bookSystemEnum, id));
    }

    @Override
    public Optional<MultiValueMap<String, String>> getDetailsFromHash(String hashedString) {
        String decrypted = decrypt(hashedString);
        return IdGeneratorUtil.getDetailsFromBookId(decrypted);
    }

    @SneakyThrows
    private String encrypt(String plainText) {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Encode to string
    }

    @SneakyThrows
    private String decrypt(String encryptedText) {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
    
    @FunctionalInterface
    public interface GenerateSecretKey {
        SecretKey apply(String key);
    }
}
