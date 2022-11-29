package com.cloud.userservice.service;

import com.cloud.userservice.dto.AuthResponse;
import com.cloud.userservice.dto.SignInRequest;
import com.cloud.userservice.dto.SignUpRequest;
import com.cloud.userservice.exception.CustomException;
import com.cloud.userservice.model.AuthModel;
import com.cloud.userservice.repository.UserRepository;
import com.cloud.userservice.util.Converter;
import com.cloud.userservice.util.ErrorMessages;
import com.cloud.userservice.util.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JWTService jwtService;

    public AuthResponse signUp(SignUpRequest signUpRequest) {
        Optional<AuthModel> byEmail = findByEmail(signUpRequest.getEmail());
        if (byEmail.isPresent()) throw new CustomException(ErrorMessages.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword()))
            throw new CustomException(ErrorMessages.PASSWORD_CONFIRM_PASSWORD_MUST_BE_SAME, HttpStatus.BAD_REQUEST);
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        AuthModel authModel = new AuthModel(Converter.fromDto(signUpRequest.getUserDetail()), signUpRequest.getEmail(), password);
        authModel.setActive(true);
        AuthModel savedUser = userRepository.save(authModel);
        String jwtToken = jwtService.createJWT(savedUser.getId().toString(), savedUser.getEmail(), savedUser.getRole().toString(), 3600000);
        return new AuthResponse(jwtToken);
    }

    private Optional<AuthModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AuthResponse signIn(SignInRequest request) throws CustomException {
        Optional<AuthModel> byEmail = findByEmail(request.getEmail());
        if (byEmail.isEmpty()) throw new CustomException(ErrorMessages.EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND);
        AuthModel savedUser = byEmail.get();
        boolean matches = passwordEncoder.matches(request.getPassword(), savedUser.getPassword());
        if (!matches) throw new CustomException(ErrorMessages.EMAIL_OR_PASSWORD_IS_WRONG, HttpStatus.CONFLICT);
        String jwtToken = jwtService.createJWT(savedUser.getId().toString(), savedUser.getEmail(), savedUser.getRole().toString(), 3600000);
        return new AuthResponse(jwtToken);
    }
}
