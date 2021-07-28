package br.com.marcello.Security.service;

import br.com.marcello.Security.model.UserAccount;
import br.com.marcello.Security.repository.UserAccountRepository;
import br.com.marcello.Security.security.PasswordEncryptionService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncryptionService passwordEncryptionService;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, RoleService roleService, PasswordEncryptionService passwordEncryptionService) {
        this.userAccountRepository = userAccountRepository;
        this.roleService = roleService;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public UserAccount createAdminAccount(String username, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword(this.passwordEncryptionService.firstTimeHashedPassword(password));
        userAccount.setRole(this.roleService.findRoleByName("ADMIN"));

        return this.userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount createAccount(UserAccount userAccount) {
        return this.userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount findAccountById(Long id) {
        return this.userAccountRepository.findById(id)
                .orElse(null);
    }

    @Override
    public UserAccount findAccountByUsername(String username) {
        return this.userAccountRepository.findAccountByUsername(username);
    }

    @Override
    public void deleteAccountByUsername(String username) {
        this.userAccountRepository.deleteAccountByUsername(username);
    }

    @Override
    public void deleteAccountByID(Long id) {
        this.userAccountRepository.deleteById(id);
    }
}
