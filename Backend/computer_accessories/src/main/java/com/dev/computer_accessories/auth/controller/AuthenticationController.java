package com.dev.computer_accessories.auth.controller;

import com.dev.computer_accessories.dto.request.PasswordRequest;
import com.dev.computer_accessories.dto.request.SignInRequest;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.TokenResponse;
import com.dev.computer_accessories.service.AuthenticationService;
import com.dev.computer_accessories.service.PasswordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final PasswordService passwordService;

    @PostMapping("/access")
    public String signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return "access";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(signInRequest), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authenticationService.register(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<TokenResponse> logout(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.logout(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest jwtToken) {
        return new ResponseEntity<>(authenticationService.refreshToken(jwtToken), HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseData<?> forgotPassword(@RequestBody PasswordRequest passwordRequest) {
        log.info("check forgot password email {}", passwordRequest.getEmail());
        passwordService.forgotPassword(passwordRequest.getEmail());
        return new ResponseData<>(HttpStatus.OK.value(), "Forgot password is successful");
    }

    @PostMapping("/change")
    public ResponseData<?> changePassword(@RequestBody PasswordRequest passwordRequest) {
        passwordService.changePassword(passwordRequest);
        return new ResponseData<>(HttpStatus.OK.value(), "Change password is successful");
    }
}
