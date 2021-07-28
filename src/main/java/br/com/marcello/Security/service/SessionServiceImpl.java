package br.com.marcello.Security.service;

import br.com.marcello.Security.dto.response.AuthenticatedAccount;
import br.com.marcello.Security.exception.LoginFailException;
import br.com.marcello.Security.model.UserAccount;
import br.com.marcello.Security.security.PasswordEncryptionService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private final UserAccountService userAccountService;
    private final PasswordEncryptionService passwordEncryptionService;

    @Value("${JWT_SECRET}")
    private String secret;

    public SessionServiceImpl(UserAccountService userAccountService, PasswordEncryptionService passwordEncryptionService) {
        this.userAccountService = userAccountService;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public AuthenticatedAccount login(String username, String password) throws LoginFailException, InvalidKeySpecException, NoSuchAlgorithmException {
        UserAccount userAccount = this.userAccountService.findAccountByUsername(username);
        AuthenticatedAccount authenticatedAccount = null;

        if(userAccount == null)
            throw new LoginFailException();

        if (this.passwordEncryptionService.assertStoredPasswordAndLoginPassword(userAccount.getPassword(), password)) {
            String token = getJWTToken(username, userAccount.getRole().getRole());
            authenticatedAccount = new AuthenticatedAccount();
            authenticatedAccount.setUsername(userAccount.getUsername());
            authenticatedAccount.setToken(token);
        }

        return authenticatedAccount;
    }

    private String getJWTToken(String username, String roleCode) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(roleCode);
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        this.secret.getBytes()).compact();

        return "Bearer " + token;
    }

    private static Claims decodeJWT(String token) {
        return Jwts.parser().setSigningKey("mySecretKey".getBytes())
                .parseClaimsJws(token).getBody();
    }

    public static String getUsername(String token) {
        Claims claims = decodeJWT(token);
        return claims.get("sub", String.class);
    }

}
