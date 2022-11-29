package com.cloud.userservice.controller;

import com.cloud.userservice.dto.AuthResponse;
import com.cloud.userservice.dto.SignInRequest;
import com.cloud.userservice.dto.SignUpRequest;
import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.exception.CustomException;
import com.cloud.userservice.service.AuthService;
import com.cloud.userservice.util.ErrorMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest{

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthService authService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class SignUpTests {
        @Test
        void user_signup_success_gives_status_201() throws Exception {
            when(authService.signUp(any(SignUpRequest.class))).thenReturn(null);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "example@email.com", "Pass@1234", "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(status().isCreated());
        }

        @Test
        void user_signup_with_invalid_email_give_error() throws Exception {
            when(authService.signUp(any(SignUpRequest.class))).thenReturn(null);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "email", "Pass@1234", "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(status().isBadRequest());
            resultActions.andExpect(jsonPath("message").value("Invalid email"));
        }

        @Test
        void should_invoke_AuthService_signUp_with_signUp_request() throws Exception {
            when(authService.signUp(any(SignUpRequest.class))).thenReturn(null);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "Pass@1234", "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            mockMvc.perform(signupReq);
            verify(authService).signUp(any(SignUpRequest.class));
        }

        @Test
        void should_give_signup__response_with_jwt_token() throws Exception {
            AuthResponse authResponse = new AuthResponse("token");
            when(authService.signUp(any(SignUpRequest.class))).thenReturn(authResponse);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "Pass@1234", "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(jsonPath("token").value(authResponse.getToken()));
        }

        @Test
        void for_signup_with_existing_email_give_error_with_already_exists_message() throws Exception {
            CustomException exception = new CustomException(ErrorMessages.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
            when(authService.signUp(any(SignUpRequest.class))).thenThrow(exception);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "Pass@1234", "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(status().isConflict());
            resultActions.andExpect(jsonPath("message").value(exception.getMessage()));
        }

        @Test
        void password_should_be_of_min_length_8() throws Exception {
            checkPasswordValidation("AS1322");
        }

        @Test
        void password_should_contain_one_capital_letter() throws Exception {
            checkPasswordValidation("asdsadas@1212");
        }

        @Test
        void password_should_contain_one_lowercase_letter() throws Exception {
            checkPasswordValidation("PASWE@1212");
        }

        @Test
        void password_should_contain_one_number() throws Exception {
            checkPasswordValidation("PASWE@apps");
        }

        @Test
        void password_should_contain_one_special_character() throws Exception {
            checkPasswordValidation("PASSE1212");
        }

        @Test
        void confirm_password_should_be_of_min_length_8() throws Exception {
            checkConfirmPasswordValidation("AS1322");
        }

        @Test
        void confirm_password_should_contain_one_capital_letter() throws Exception {
            checkConfirmPasswordValidation("asdsadas@1212");
        }

        @Test
        void confirm_password_should_contain_one_lowercase_letter() throws Exception {
            checkConfirmPasswordValidation("PASWE@1212");
        }

        @Test
        void confirm_password_should_contain_one_number() throws Exception {
            checkConfirmPasswordValidation("PASWE@apps");
        }

        @Test
        void confirm_password_should_contain_one_special_character() throws Exception {
            checkConfirmPasswordValidation("PASI91212");
        }

        private void checkPasswordValidation(String password) throws Exception {
            CustomException exception = new CustomException(ErrorMessages.PASSWORD_INVALID,HttpStatus.BAD_REQUEST);
            when(authService.signUp(any(SignUpRequest.class))).thenThrow(exception);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", password, "Pass@1234");
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(status().isBadRequest());
            resultActions.andExpect(jsonPath("message").value(exception.getMessage()));
        }

        private void checkConfirmPasswordValidation(String confirmPassword) throws Exception {
            CustomException exception = new CustomException(ErrorMessages.PASSWORD_INVALID,HttpStatus.BAD_REQUEST);
            when(authService.signUp(any(SignUpRequest.class))).thenThrow(exception);
            MockHttpServletRequestBuilder signupReq = MockMvcRequestBuilders.post("/signup");
            signupReq.contentType(MediaType.APPLICATION_JSON);
            SignUpRequest signUpRequest = new SignUpRequest(new UserDetailDto(), "user@email.com", "Pass@1234", confirmPassword);
            signupReq.content(objectMapper.writeValueAsString(signUpRequest));
            ResultActions resultActions = mockMvc.perform(signupReq);
            resultActions.andExpect(status().isBadRequest());
            resultActions.andExpect(jsonPath("message").value(exception.getMessage()));
        }
    }

    @Nested
    class SignInTests {
        @Test
        void should_give_status_200_when_sign_in_success() throws Exception {
            SignInRequest request = new SignInRequest("example@email.com", "Pass@1234");
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            signInRequest.content(objectMapper.writeValueAsString(request));
            ResultActions perform = mockMvc.perform(signInRequest);
            perform.andExpect(status().isOk());
        }

        @Test
        void should_invoke_AuthService_signin_with_signin_request() throws Exception {
            SignInRequest request = new SignInRequest("example@email.com", "Pass@1234");
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            signInRequest.content(objectMapper.writeValueAsString(request));
            mockMvc.perform(signInRequest);
            verify(authService).signIn(request);
        }

        @Test
        void should_return_token_when_login_successful() throws Exception {
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            SignInRequest request = new SignInRequest("user@email.com", "Pass@1234");
            AuthResponse authResponse = new AuthResponse("token");
            when(authService.signIn(any(SignInRequest.class))).thenReturn(authResponse);
            signInRequest.content(objectMapper.writeValueAsString(request));
            ResultActions perform = mockMvc.perform(signInRequest);
            perform.andExpect(jsonPath("token").value(authResponse.getToken()));
        }

        @Test
        void should_give_error_when_email_is_invalid() throws Exception {
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            SignInRequest request = new SignInRequest("user.com", "Pass@1234");
            AuthResponse authResponse = new AuthResponse("token");
            when(authService.signIn(any(SignInRequest.class))).thenReturn(authResponse);
            signInRequest.content(objectMapper.writeValueAsString(request));
            ResultActions perform = mockMvc.perform(signInRequest);
            perform.andExpect(jsonPath("message").value(ErrorMessages.EMAIL_INVALID));
        }

        @Test
        void should_give_error_when_email_is_null() throws Exception {
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            SignInRequest request = new SignInRequest(null, "Pass@1234");
            AuthResponse authResponse = new AuthResponse("token");
            when(authService.signIn(any(SignInRequest.class))).thenReturn(authResponse);
            signInRequest.content(objectMapper.writeValueAsString(request));
            ResultActions perform = mockMvc.perform(signInRequest);
            perform.andExpect(jsonPath("message").value(ErrorMessages.EMAIL_MUST_NOT_BE_EMPTY));
        }

        @Test
        void should_give_error_when_password_is_null() throws Exception {
            MockHttpServletRequestBuilder signInRequest = MockMvcRequestBuilders.post("/signin");
            signInRequest.contentType(MediaType.APPLICATION_JSON);
            SignInRequest request = new SignInRequest("user@email.com", null);
            AuthResponse authResponse = new AuthResponse("token");
            when(authService.signIn(any(SignInRequest.class))).thenReturn(authResponse);
            signInRequest.content(objectMapper.writeValueAsString(request));
            ResultActions perform = mockMvc.perform(signInRequest);
            perform.andExpect(jsonPath("message").value(ErrorMessages.PASSWORD_MUST_NOT_BE_EMPTY));
        }
    }

}