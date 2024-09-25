package com.dmsc.libraryserviceapi.service.hashing;

import com.dmsc.libraryserviceapi.exception.LibraryInvalidDataException;
import com.dmsc.libraryserviceapi.model.book.BookSystemEnum;
import com.dmsc.libraryserviceapi.util.IdGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * This class will be responsible to implement AES to hash an identifier.
 * Recurring to autoconfigure and properties this implementation can be configured to via properties by having multiple implementations
 */
public class AESIdentifierHashImpl implements IdentifierHashService {
    private static final Logger LOG = LoggerFactory.getLogger(AESIdentifierHashImpl.class);

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

    private String encrypt(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getUrlEncoder().encodeToString(encryptedBytes); // Encode to string
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | RuntimeException e) {
            LOG.error("Unable to encrypt the text", e);
            throw new LibraryInvalidDataException("Error processing data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String decrypt(String encryptedText) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | RuntimeException e) {
            LOG.error("Unable to decrypt the encrypted text", e);
            throw new LibraryInvalidDataException("Error processing data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @FunctionalInterface
    public interface GenerateSecretKey {
        SecretKey apply(String key);
    }
}
