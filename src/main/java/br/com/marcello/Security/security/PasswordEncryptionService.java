package br.com.marcello.Security.security;

import br.com.marcello.Security.exception.LoginFailException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface PasswordEncryptionService {

    String firstTimeHashedPassword(String plainPassword) throws InvalidKeySpecException, NoSuchAlgorithmException;

    Boolean assertStoredPasswordAndLoginPassword(String storedPassword, String loginPassword) throws InvalidKeySpecException, NoSuchAlgorithmException, LoginFailException;

}
