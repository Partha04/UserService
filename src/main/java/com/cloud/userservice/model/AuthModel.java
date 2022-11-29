package com.cloud.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class AuthModel implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne(targetEntity = UserDetail.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private UserDetail userDetail;
    @Column(unique = true)
    private String email;
    private String password;

    private Boolean active;

    private Role role = Role.USER;

    public AuthModel(UserDetail userDetail, String email, String password) {
        this.userDetail = userDetail;
        this.email = email;
        this.password = password;
    }
}
