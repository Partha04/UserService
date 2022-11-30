package com.cloud.userservice.service;

import com.cloud.userservice.dto.AuthResponse;
import com.cloud.userservice.dto.SignInRequest;
import com.cloud.userservice.dto.SignUpRequest;
import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.exception.CustomException;
import com.cloud.userservice.model.UserModel;
import com.cloud.userservice.model.Role;
import com.cloud.userservice.model.UserDetail;
import com.cloud.userservice.repository.UserRepository;
import com.cloud.userservice.testUtils.PostgresTestContainer;
import com.cloud.userservice.util.JWTService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest extends PostgresTestContainer {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JWTService jwtService;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    class SignUpServiceTest {
        @Test
        void new_user_sign_up_gives_token() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "", "", "");
            AuthResponse authResponse = authService.signUp(signUpRequest);
            Assertions.assertNotNull(authResponse.getToken());
        }

        @Test
        public void should_save_user_in_user_db() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "password");
            authService.signUp(signUpRequest);
            assertTrue(userRepository.findByEmail(signUpRequest.getEmail()).isPresent());
        }

        @Test
        public void should_save_user_as_active_in_user_db() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "password");
            authService.signUp(signUpRequest);
            Optional<UserModel> byEmail = userRepository.findByEmail(signUpRequest.getEmail());
            assertTrue(byEmail.isPresent());
            assertTrue(byEmail.get().getActive());
        }


        @Test
        void should_check_for_existing_email() {
            userRepository.save(new UserModel(new UserDetail("fn","ln"), "userExist@email.com", "1212"));
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "userExist@email.com", "password", "password");
            CustomException customException = assertThrows(CustomException.class, () -> authService.signUp(signUpRequest));
            assertEquals("email already exists", customException.getMessage());
        }

        @Test
        void should_give_error_when_password_and_confirmed_password_not_equal() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "confirm password");
            CustomException customException = assertThrows(CustomException.class, () -> authService.signUp(signUpRequest));
            assertEquals("password and confirm passwords must be same", customException.getMessage());
        }

        @Test
        void should_save_the_password_in_encrypted_form_in_DB() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "password");
            authService.signUp(signUpRequest);
            Optional<UserModel> byEmail = userRepository.findByEmail(signUpRequest.getEmail());
            assertTrue(passwordEncoder.matches(signUpRequest.getPassword(), byEmail.isPresent() ? byEmail.get().getPassword() : ""));
        }

        @Test
        void should_generate_a_new_JWT_token_with_60_min_validity() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "password");
            AuthResponse authResponse = authService.signUp(signUpRequest);
            String token = authResponse.getToken();
            Claims claims = jwtService.decodeJWT(token);
            assertNotNull(claims.getId());
            assertEquals(signUpRequest.getEmail(), claims.getIssuer());
            assertEquals("USER", claims.getSubject());
            assertTrue(1 <= claims.getExpiration().compareTo(new Date()));
        }

        @Test
        void should_give_the_new_user_user_role_when_sign_up() {
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "password", "password");
            authService.signUp(signUpRequest);
            Optional<UserModel> byEmail = userRepository.findByEmail(signUpRequest.getEmail());
            assertTrue(byEmail.isPresent());
            Role roles = byEmail.get().getRole();
            assertEquals(Role.USER, roles);
        }

    }

    @Nested
    class SignInServiceTest {
        @Test
        void should_give_a_token_when_user_is_valid() throws CustomException {
            UserDetail userDetail = new UserDetail("firstName","lastName");
            String email = "user@email.com";
            String encodeedPassword = passwordEncoder.encode("Pass@1234");
            String password = "Pass@1234";
            userRepository.save(new UserModel(userDetail, email, encodeedPassword));
            SignInRequest signInRequest = new SignInRequest(email, password);
            AuthResponse signInResponse = authService.signIn(signInRequest);
            assertNotNull(signInResponse.getToken());
        }

        @Test
        void should_give_error_when_user_email_not_found() {
            String email = "user@email.com";
            String password = "Pass@1234";
            SignInRequest signInRequest = new SignInRequest(email, password);
            CustomException customException = assertThrows(CustomException.class, () -> authService.signIn(signInRequest));
            assertEquals("Email not found", customException.getMessage());
        }

        @Test
        void should_give_error_when_the_password_is_wrong() {
            String email = "user@email.com";
            String password = passwordEncoder.encode("Pass@1234");
            userRepository.save(new UserModel( new UserDetail("fn","ln"), email, password));
            SignInRequest signInRequest = new SignInRequest(email, "wrongPassword");
            CustomException customException = assertThrows(CustomException.class, () -> authService.signIn(signInRequest));
            assertEquals("Email or password is wrong", customException.getMessage());
        }

        @Test
        void should_give_a_token_when_signin_is_successful() throws CustomException {
            String email = "user@email.com";
            String password = "Pass@1234";
            String encodedPassword = passwordEncoder.encode(password);
            UserModel userModel = userRepository.save(new UserModel(new UserDetail("fn","ln"), email, encodedPassword));
            SignInRequest signInRequest = new SignInRequest(email, password);
            AuthResponse authResponse = authService.signIn(signInRequest);
            String token = authResponse.getToken();
            Claims claims = jwtService.decodeJWT(token);
            assertEquals(userModel.getId().toString(), claims.getId());
            assertEquals(userModel.getEmail(), claims.getIssuer());
            assertEquals("USER", claims.getSubject());
        }
    }

}