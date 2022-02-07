package com.sellertool.server.config.exception;

public class AuthorizationAccessDeniedException extends RuntimeException{
    public AuthorizationAccessDeniedException(String msg) {
        super(msg);
    }
    public AuthorizationAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
