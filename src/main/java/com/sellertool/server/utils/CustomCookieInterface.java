package com.sellertool.server.utils;

public interface CustomCookieInterface {
    final static String COOKIE_DOMAIN = "localhost"; // PROD : viewday.co.kr | DEV : localhost

    final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5 * 24 * 60 * 60; // seconds - 5일
    final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5; // seconds - 5s
}
