package br.com.marcello.Security.controller;

import br.com.marcello.Security.dto.AccountDto;
import br.com.marcello.Security.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/createAdminUser")
    @Secured("ADMIN")
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return new ResponseEntity<>(this.userAccountService.createAdminAccount(accountDto.getUsername(),
                accountDto.getPassword()), HttpStatus.OK);
    }

}
