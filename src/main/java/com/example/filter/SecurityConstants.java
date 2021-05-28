package com.example.filter;

public final class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/api/authenticate";
    public static final long EXPIRATIONTIME = 864_000_000; // 10 days
    public static final String SECRET = "ThisIsASecret";
    public static final String HEADER_STRING = "Authorization";


    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";
    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
