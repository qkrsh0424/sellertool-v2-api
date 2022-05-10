package com.sellertool.server.domain.exception.dto;

public class CustomExcelFileUploadException extends RuntimeException {
    public CustomExcelFileUploadException(String message) {
        super(message);
    }

    public CustomExcelFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
