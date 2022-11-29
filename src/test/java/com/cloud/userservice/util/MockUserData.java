package com.cloud.userservice.util;

import com.cloud.userservice.dto.AddressDto;
import com.cloud.userservice.dto.ContactDto;
import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.model.*;

import java.util.ArrayList;

public class MockUserData {
    public static UserDetailDto userDetailDto = new UserDetailDto();
    public static UserDetail userDetail = new UserDetail();
    public static String userFirstName = "userFirstName";
    public static String userLastName = "userLastName";
    public static String addressLine1 = "Address line 1";
    public static String addressLine2 = "Address line 2";
    public static String state = "user state";
    public static String pinCode = "232323";
    public static String district = "user district";
    public static String countryCode = "+91";
    public static String contactValue = "7411122233";
    public static AuthModel authModel = new AuthModel();
    public static boolean active = true;
    public static String userEmail = "user@example.com";
    public static String password = "demopassword";

    static {
        userDetailDto.setFirstName(userFirstName);
        userDetailDto.setLastName(userLastName);
        ArrayList<AddressDto> addresses = new ArrayList<>();
        AddressDto addressDto = new AddressDto();
        addressDto.setLine1(addressLine1);
        addressDto.setLine2(addressLine2);
        addressDto.setState(state);
        addressDto.setPinCode(pinCode);
        addressDto.setDistrict(district);
        addresses.add(addressDto);
        userDetailDto.setAddresses(addresses);
        ArrayList<ContactDto> contacts = new ArrayList<>();
        ContactDto contactDto = new ContactDto();
        contactDto.setCountryCode(countryCode);
        contactDto.setValue(contactValue);
        contacts.add(contactDto);
        userDetailDto.setContacts(contacts);


        userDetail.setFirstName(userFirstName);

        userDetail.setLastName(userLastName);

        ArrayList<Address> addresses1 = new ArrayList<>();
        Address address = new Address();
        address.setLine1(addressLine1);
        address.setLine2(addressLine2);
        address.setDistrict(district);
        address.setState(state);
        address.setPinCode(pinCode);
        addresses1.add(address);
        userDetail.setAddresses(addresses1);

        ArrayList<Contact> userContacts = new ArrayList<>();
        Contact contact = new Contact();
        contact.setCountryCode(countryCode);
        contact.setValue(contactValue);
        userContacts.add(contact);
        userDetail.setContacts(userContacts);

        authModel.setUserDetail(userDetail);
        authModel.setRole(Role.USER);
        authModel.setActive(active);
        authModel.setEmail(userEmail);
        authModel.setPassword(password);
    }
}
