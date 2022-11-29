package com.cloud.userservice.repository;

import com.cloud.userservice.model.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AuthModel, UUID> {
    Optional<AuthModel> findByEmail(String email);
}
