package com.cloud.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="address")
@NoArgsConstructor
@Getter@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String line1;
    private String line2;
    private String landMark;
    private String district;
    private String state;
    private String pinCode;
}
