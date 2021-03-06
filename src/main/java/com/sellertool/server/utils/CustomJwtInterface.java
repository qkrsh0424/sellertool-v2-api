package com.sellertool.server.utils;

public interface CustomJwtInterface {
    final static Integer JWT_TOKEN_EXPIRATION = 20*60*1000;  // milliseconds - 20분

    final static Integer REFRESH_TOKEN_JWT_EXPIRATION = 5*24*60*60*1000;   // milliseconds - 5일

    final static Integer CSRF_TOKEN_JWT_EXPIRATION = 5 * 1000;  // milliseconds - 5000ms -> 5s
}
