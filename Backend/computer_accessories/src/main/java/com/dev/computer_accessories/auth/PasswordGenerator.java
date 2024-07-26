package com.dev.computer_accessories.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PasswordGenerator {

    // Các ký tự có thể sử dụng để tạo mật khẩu
    private final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private final String NUMBER = "0123456789";
    private final String OTHER_CHAR = "!@#$%&*()_+-=[]|,./?><";

    private final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;

    private final SecureRandom random = new SecureRandom();

    /**
     * Hàm tạo mật khẩu ngẫu nhiên với độ dài cho trước.
     * @param length Độ dài của mật khẩu cần tạo
     * @return Mật khẩu ngẫu nhiên
     */
    public String generateRandomPassword(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length of password must be positive.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(PASSWORD_ALLOW_BASE.length());
            sb.append(PASSWORD_ALLOW_BASE.charAt(randomIndex));
        }
        return sb.toString();
    }
}
