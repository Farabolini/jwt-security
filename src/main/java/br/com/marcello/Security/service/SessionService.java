package br.com.marcello.Security.service;

import br.com.marcello.Security.dto.response.AuthenticatedAccount;
import br.com.marcello.Security.exception.LoginFailException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface SessionService {

    AuthenticatedAccount login(String username, String password) throws LoginFailException, InvalidKeySpecException, NoSuchAlgorithmException;

}
