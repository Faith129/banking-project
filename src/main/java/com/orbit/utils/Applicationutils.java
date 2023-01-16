package com.orbit.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Applicationutils {
    public static final String SUCCESSFULL_WITHDRAWAL = "your withdrawal was successful";
    public static final String SUCCESSFULL_DEPOSIT = "your account has been successfully credited";
    public static final String SUCCESSFUL_TRANSFER = "your transfer was successful";
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "1234567890987654321";
    private static final int ACCOUNT_NUMBER_LIMIT = 10;

    public static String generateUniqueAccountNumber() {
        StringBuilder main = new StringBuilder(ACCOUNT_NUMBER_LIMIT);
        for (int i = 0; i < ACCOUNT_NUMBER_LIMIT; i++) {
            main.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return main.toString();
    }
}
