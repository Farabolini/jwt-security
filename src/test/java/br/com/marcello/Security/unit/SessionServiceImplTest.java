package br.com.marcello.Security.unit;

import br.com.marcello.Security.dto.response.AuthenticatedAccount;
import br.com.marcello.Security.exception.LoginFailException;
import br.com.marcello.Security.model.Role;
import br.com.marcello.Security.model.UserAccount;
import br.com.marcello.Security.repository.RoleRepository;
import br.com.marcello.Security.repository.UserAccountRepository;
import br.com.marcello.Security.security.PasswordEncryptionServiceImpl;
import br.com.marcello.Security.service.RoleServiceImpl;
import br.com.marcello.Security.service.SessionServiceImpl;
import br.com.marcello.Security.service.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SessionServiceImplTest {

    UserAccountRepository userAccountRepository = Mockito.mock(UserAccountRepository.class);
    RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    PasswordEncryptionServiceImpl passwordEncryptionService;
    UserAccountServiceImpl userAccountService;
    RoleServiceImpl roleService;
    SessionServiceImpl sessionService;

    @BeforeEach
    void setup() {
        this.passwordEncryptionService = new PasswordEncryptionServiceImpl();
        this.roleService = new RoleServiceImpl(roleRepository);
        this.userAccountService = new UserAccountServiceImpl(userAccountRepository, roleService, passwordEncryptionService);
        this.sessionService = new SessionServiceImpl(userAccountService, passwordEncryptionService);
        ReflectionTestUtils.setField(sessionService,"secret", "mySecretKey");
    }

    @Test
    void shouldLoginSuccessful() throws Exception {
        Role role = new Role(1L,"ADMIN", null);
        UserAccount userAccount = new UserAccount(1L, "adminTest",
                this.passwordEncryptionService.firstTimeHashedPassword("teste10"), role);
        List<UserAccount> userAccounts = new ArrayList<>();
        userAccounts.add(userAccount);
        role.setUserAccounts(userAccounts);
        when(this.userAccountRepository.findAccountByUsername("adminTest")).thenReturn(userAccount);
        AuthenticatedAccount authenticatedAccount = this.sessionService.login("adminTest", "teste10");
        assertEquals(authenticatedAccount.getUsername(), "adminTest");
    }

    @Test
    void shouldThrowLoginFailException() throws Exception {
        assertThrows(LoginFailException.class, () -> this.sessionService.login("userTest", "teste10"),
                "Invalid username or password.");
    }

}
