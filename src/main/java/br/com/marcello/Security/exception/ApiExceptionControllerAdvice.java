package br.com.marcello.Security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionControllerAdvice {

    @ExceptionHandler(LoginFailException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiException> loginFailHandler(LoginFailException e) {
        ApiException apiException = new ApiException();
        apiException.setDescription(e.getMessage());
        apiException.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiValidationException> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        List<FormatError> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::mapError)
                .collect(Collectors.toList());

        ApiValidationException apiValidationException = new ApiValidationException().toBuilder()
                .message("Validation errors, please see messages below")
                .errors(errors)
                .build();

        return new ResponseEntity<>(apiValidationException, HttpStatus.BAD_REQUEST);
    }

    private FormatError mapError(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            return new FormatError(((FieldError) objectError).getField(),
                    objectError.getDefaultMessage());
        }
        return new FormatError(objectError.getObjectName(), objectError.getDefaultMessage());
    }

}
