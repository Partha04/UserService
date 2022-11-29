package com.cloud.userservice.service;

import com.cloud.userservice.dto.AddressDto;
import com.cloud.userservice.dto.ContactDto;
import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.exception.CustomException;
import com.cloud.userservice.model.AuthModel;
import com.cloud.userservice.repository.UserRepository;
import com.cloud.userservice.testUtils.PostgresTestContainer;
import com.cloud.userservice.util.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.cloud.userservice.util.MockUserData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest extends PostgresTestContainer {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    UUID id;

    @BeforeEach
    void setUp() {
        AuthModel authModel1=userRepository.save(authModel);
        id = authModel1.getId();
    }

    @Nested
    class GetUserDetails {
        @Test
        void shouldGiveUserDetailsIfExists() {
            UserDetailDto userDetailByID = userService.getUserDetailByID(id);
            assertEquals(userFirstName, userDetailByID.getFirstName());
            assertEquals(userLastName, userDetailByID.getLastName());
            AddressDto addressDto = userDetailByID.getAddresses().get(0);
            assertEquals(addressLine1, addressDto.getLine1());
            assertEquals(addressLine2, addressDto.getLine2());
            assertEquals(district, addressDto.getDistrict());
            assertEquals(state, addressDto.getState());
            assertEquals(pinCode, addressDto.getPinCode());

            ContactDto contactDto = userDetailByID.getContacts().get(0);
            assertEquals(countryCode, contactDto.getCountryCode());
            assertEquals(contactDto.getValue(), contactDto.getValue());
            assertEquals(contactDto.getValue(), contactDto.getValue());
        }

        @Test
        void shouldGiveErrorWhenUserIdIsNotPresent() {
            UUID randomUUID = UUID.randomUUID();
            CustomException entityNotFoundException = assertThrows(CustomException.class, () -> userService.getUserDetailByID(randomUUID));
            assertEquals(ErrorMessages.ENTITY_NOT_FOUND + randomUUID, entityNotFoundException.getMessage());
            assertEquals(HttpStatus.NOT_FOUND,entityNotFoundException.getStatus());
        }
    }
}