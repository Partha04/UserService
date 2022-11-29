package com.cloud.userservice.controller;

import com.cloud.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static com.cloud.userservice.util.MockUserData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Nested
    class GetUserDetail {
        @Test
        void shouldGiveErrorWhenHeadersAreNotPresent() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userDetail");
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            resultActions.andExpect(status().isBadRequest());
        }

        @Test
        void userCanGetOwnDetailsGivesOkStatus() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userDetail");
            HttpHeaders httpHeaders = new HttpHeaders();
            String id = UUID.randomUUID().toString();
            httpHeaders.add("id", id);
            httpHeaders.add("role", "USER");
            requestBuilder.headers(httpHeaders);
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            resultActions.andExpect(status().isOk());
        }

        @Test
        void shouldInvokeUserService_getUserDetailByIDMethod() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userDetail");
            HttpHeaders httpHeaders = new HttpHeaders();
            String id = UUID.randomUUID().toString();
            httpHeaders.add("id", id);
            httpHeaders.add("role", "USER");
            requestBuilder.headers(httpHeaders);
            mockMvc.perform(requestBuilder);
            verify(userService).getUserDetailByID(UUID.fromString(id));
        }

        @Test
        void shouldGiveUserDetailForGivenUserID() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/userDetail");
            HttpHeaders httpHeaders = new HttpHeaders();
            UUID uuid = UUID.randomUUID();
            String id = uuid.toString();
            httpHeaders.add("id", id);
            httpHeaders.add("role", "USER");
            requestBuilder.headers(httpHeaders);
            when(userService.getUserDetailByID(uuid)).thenReturn(userDetailDto);
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            resultActions.andExpect(jsonPath("firstName").value(userFirstName));
            resultActions.andExpect(jsonPath("lastName").value(userLastName));
            resultActions.andExpect(jsonPath("addresses[0].line1").value(addressLine1));
            resultActions.andExpect(jsonPath("addresses[0].line2").value(addressLine2));
            resultActions.andExpect(jsonPath("addresses[0].state").value(state));
            resultActions.andExpect(jsonPath("addresses[0].pinCode").value(pinCode));
            resultActions.andExpect(jsonPath("addresses[0].district").value(district));
            resultActions.andExpect(jsonPath("contacts[0].countryCode").value(countryCode));
            resultActions.andExpect(jsonPath("contacts[0].value").value(contactValue));
        }

    }

    @Nested
    class updateUserDetail {

        ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void shouldGiveErrorWhenHeadersAreNotPresent() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/userDetail");
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            resultActions.andExpect(status().isBadRequest());
        }

        @Test
        void shouldGiveStatusOkWhenStatusIsUpdatedSuccessfully() throws Exception {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/userDetail");
            requestBuilder.content(objectMapper.writeValueAsString(userDetailDto));
            HttpHeaders httpHeaders = new HttpHeaders();
            String id = UUID.randomUUID().toString();
            httpHeaders.add("id", id);
            httpHeaders.add("role", "USER");
            requestBuilder.headers(httpHeaders);
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            resultActions.andExpect(status().isOk());
        }
    }

}
