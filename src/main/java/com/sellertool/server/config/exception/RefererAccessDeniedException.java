package com.sellertool.server.config.exception;

public class RefererAccessDeniedException extends RuntimeException{
    public RefererAccessDeniedException(String msg) {
        super(msg);
    }
    public RefererAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
