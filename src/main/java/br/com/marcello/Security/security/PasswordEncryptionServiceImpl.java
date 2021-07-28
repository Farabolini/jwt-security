package br.com.marcello.Security.security;

import br.com.marcello.Security.exception.LoginFailException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService {

    private final Base64.Decoder decoder = Base64.getDecoder();
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final int SALT_HALF_LENGTH = 12;

    @Override
    public Boolean assertStoredPasswordAndLoginPassword(String storedPassword, String loginPassword)
            throws InvalidKeySpecException, NoSuchAlgorithmException, LoginFailException {
        byte[] userSalt = this.getSaltFromStoredPassword(storedPassword);
        String userGeneratedPassword = encoder.encodeToString(this.hashPassword(userSalt, loginPassword));
        String userHashedPassword = this.getHashFromStoredPassword(storedPassword);

        if(!userGeneratedPassword.equals(userHashedPassword))
            throw new LoginFailException();

        return true;
    }

    @Override
    public String firstTimeHashedPassword(String plainPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = this.generateSalt();
        byte[] hash = this.hashPassword(salt, plainPassword);
        return this.mixHashedPasswordAndSalt(hash, salt);
    }

    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(byte[] salt, String plainPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        int NUMBER_OF_ITERATIONS = 65536;
        KeySpec keySpec = new PBEKeySpec(plainPassword.toCharArray(), salt, NUMBER_OF_ITERATIONS,128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return keyFactory.generateSecret(keySpec).getEncoded();
    }

    private String mixHashedPasswordAndSalt(byte[] hash, byte[] salt) {
        StringBuilder sb = new StringBuilder();
        String hashString = this.encoder.encodeToString(hash);
        String saltString = this.encoder.encodeToString(salt);

        return sb.append(saltString.substring(0, this.SALT_HALF_LENGTH))
                .append(hashString)
                .append(saltString.substring(this.SALT_HALF_LENGTH))
        .toString();
    }

    private byte[] getSaltFromStoredPassword(String storedPassword) {
        return this.decoder.decode((storedPassword.substring(0, this.SALT_HALF_LENGTH) +
                storedPassword.substring(storedPassword.length() - this.SALT_HALF_LENGTH)));
    }

    private String getHashFromStoredPassword(String storedPassword) {
        return storedPassword.substring(this.SALT_HALF_LENGTH, (storedPassword.length() - this.SALT_HALF_LENGTH));
    }

}
