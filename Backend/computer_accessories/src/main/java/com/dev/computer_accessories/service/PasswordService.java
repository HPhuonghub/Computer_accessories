package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.PasswordRequest;

public interface PasswordService {
    void forgotPassword(String email);

    void changePassword(PasswordRequest passwordRequest);
}
