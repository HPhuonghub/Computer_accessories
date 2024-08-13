package com.dev.computer_accessories.auth.controller;

import com.dev.computer_accessories.dto.request.SignInRequest;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Slf4j
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
    public String logout() {
        return "access";
    }

    @PostMapping("/refresh")
    public String refresh() {
        return "access";
    }
}
