package com.cloud.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_detail")
@NoArgsConstructor
@Getter
@Setter
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String firstName;
    private String lastName;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Address.class)
    private List<Address> addresses;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Contact.class)
    private List<Contact> contacts;

    public UserDetail(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
