package com.dev.computer_accessories.auth.controller;

import com.dev.computer_accessories.auth.request.PasswordRequest;
import com.dev.computer_accessories.auth.service.PasswordService;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseData<?> forgotPassword(@RequestBody PasswordRequest passwordRequest) {
        try {
            log.info("check forgot password email {}", passwordRequest.getEmail());
            passwordService.forgotPassword(passwordRequest.getEmail());
            return new ResponseData<>(HttpStatus.OK.value(), "Forgot password is successful");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseData<?> changePassword(@RequestBody PasswordRequest passwordRequest) {
        try {
            passwordService.changePassword(passwordRequest);
            return new ResponseData<>(HttpStatus.OK.value(), "Change password is successful");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
