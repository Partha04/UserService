package com.cloud.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailDto {
    private String firstName;
    private String lastName;
    private List<AddressDto> addresses;
    private List<ContactDto> contacts;
}
