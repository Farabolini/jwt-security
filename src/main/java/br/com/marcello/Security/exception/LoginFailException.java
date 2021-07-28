package br.com.marcello.Security.exception;

public class LoginFailException extends Exception {

    private static final String MESSAGE = "Invalid username or password.";

    public LoginFailException() {
        super(MESSAGE);
    }

}
