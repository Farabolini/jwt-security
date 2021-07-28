package br.com.marcello.Security.controller;

import br.com.marcello.Security.dto.AccountDto;
import br.com.marcello.Security.dto.response.AuthenticatedAccount;
import br.com.marcello.Security.exception.ApiException;
import br.com.marcello.Security.exception.LoginFailException;
import br.com.marcello.Security.model.UserAccount;
import br.com.marcello.Security.service.SessionServiceImpl;
import br.com.marcello.Security.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Tag(name = "Login Controller", description = "Login endpoint")
@Validated
@RestController
@RequestMapping
public class LoginController {

    private final SessionServiceImpl sessionService;

    public LoginController(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Account login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticatedAccount.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid Login request.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiException.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal Server error.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiException.class))
                    })
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AccountDto accountDto) throws InvalidKeySpecException, NoSuchAlgorithmException, LoginFailException {
        return new ResponseEntity<>(this.sessionService.login(accountDto.getUsername(),
                accountDto.getPassword()), HttpStatus.OK);
    }

}
