package com.cloud.userservice.dto;

import com.cloud.userservice.util.ErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class SignUpRequest {
    private UserDetailDto userDetail;
    @NotNull(message = ErrorMessages.EMAIL_MUST_NOT_BE_EMPTY)
    @Email(message = ErrorMessages.EMAIL_INVALID)
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = ErrorMessages.PASSWORD_INVALID)
    private String password;
    @NotNull
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = ErrorMessages.PASSWORD_INVALID)
    private String confirmPassword;
}
