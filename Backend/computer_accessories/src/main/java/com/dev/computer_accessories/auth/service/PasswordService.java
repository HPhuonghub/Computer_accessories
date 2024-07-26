package com.dev.computer_accessories.auth.service;

import com.dev.computer_accessories.auth.request.PasswordRequest;

public interface PasswordService {
    void forgotPassword(String email);

    void changePassword(PasswordRequest passwordRequest);
}
