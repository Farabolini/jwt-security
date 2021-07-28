package br.com.marcello.Security.service;

import br.com.marcello.Security.dto.AccountDto;
import br.com.marcello.Security.model.UserAccount;
import org.springframework.data.repository.query.Param;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserAccountService {

    UserAccount createAccount(UserAccount userAccount);

    UserAccount findAccountById(Long id);

    UserAccount findAccountByUsername(@Param("username") String username);

    void deleteAccountByUsername(@Param("username") String username);

    void deleteAccountByID(Long id);

    UserAccount createAdminAccount(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException;


}
