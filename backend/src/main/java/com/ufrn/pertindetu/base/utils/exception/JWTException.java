package com.ufrn.pertindetu.base.utils.exception;


import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling JWT (JSON Web Token) related errors.
 * <p>
 * This exception carries an HTTP status code to allow appropriate response handling
 * in web applications. thrown when there is an authentication or
 * authorization issue related to JWT processing.
 */
public class JWTException extends BusinessException {

    /**
     * Constructs a new JWTException with the specified detail message and HTTP
     * status code.
     *
     * @param message    the detail message.
     * @param statusCode the HTTP status code associated with the error.
     */
    public JWTException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }


}
