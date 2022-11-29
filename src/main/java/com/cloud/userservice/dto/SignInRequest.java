package com.cloud.userservice.dto;

import com.cloud.userservice.util.ErrorMessages;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignInRequest {
    @Email(message = ErrorMessages.EMAIL_INVALID )
    @NotNull(message = ErrorMessages.EMAIL_MUST_NOT_BE_EMPTY)
    private String email;
    @NotNull(message = ErrorMessages.PASSWORD_MUST_NOT_BE_EMPTY)
    private String password;
}
