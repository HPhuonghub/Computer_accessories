package com.dev.computer_accessories.auth.service.impl;

import com.dev.computer_accessories.auth.PasswordGenerator;
import com.dev.computer_accessories.auth.request.PasswordRequest;
import com.dev.computer_accessories.auth.service.MailMessage;
import com.dev.computer_accessories.auth.service.PasswordService;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordGenerator passwordGenerator;
    private final MailMessage mailMessage;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void forgotPassword(String email) {


        if (!userService.existEmail(email)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        String newPassword = passwordGenerator.generateRandomPassword(12);
        System.out.println("Check newPassword: " + newPassword);
        userService.changePassword(email, newPassword);
        String subject = "Reset Password";
        String body = "Dear " + email + ", "
                + "Your password has been reset successfully. Below is your new password: "
                + newPassword
                + " Please log in with this new password and consider changing it immediately. "
                + " Best regards, Your Application Team";
        mailMessage.sendMessage(email, subject, body);

    }

    @Override
    public void changePassword(PasswordRequest passwordRequest) {

        if (passwordRequest.getNewPassword().length() < 6) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        User user = userService.getUserByEmail(passwordRequest.getEmail());

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }


        userService.changePassword(passwordRequest.getEmail(), passwordRequest.getNewPassword());

    }

}
