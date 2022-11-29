package com.cloud.userservice.controller;

import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/userDetail")
    ResponseEntity<UserDetailDto> getUserDetails(@RequestHeader("id") String id) {
        UserDetailDto userDetailByID = userService.getUserDetailByID(UUID.fromString(id));
        return ResponseEntity.ok(userDetailByID);
    }
    @PutMapping("/userDetail")
    ResponseEntity<UserDetailDto> updateUserDetails(@RequestHeader("id") String id) {
        return ResponseEntity.ok(null);
    }


}
