package br.com.marcello.Security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotBlank(message = "Username is obligatory.")
    @Pattern(regexp = "^[A-za-z\\s]*$", message = "Only letters allowed")
    private String username;

    @NotBlank(message = "Password is obligatory.")
    private String password;

}
