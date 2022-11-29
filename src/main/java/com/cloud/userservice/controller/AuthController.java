package com.cloud.userservice.controller;

import com.cloud.userservice.dto.AuthResponse;
import com.cloud.userservice.dto.SignInRequest;
import com.cloud.userservice.dto.SignUpRequest;
import com.cloud.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/signup")
    ResponseEntity<AuthResponse> signUpNewUser(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {
        AuthResponse authResponse = authService.signUp(signUpRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/signin")
    ResponseEntity<AuthResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) throws Exception {
        AuthResponse authResponse = authService.signIn(signInRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
