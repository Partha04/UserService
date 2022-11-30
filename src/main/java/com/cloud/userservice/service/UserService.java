package com.cloud.userservice.service;

import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.exception.CustomException;
import com.cloud.userservice.model.UserModel;
import com.cloud.userservice.repository.UserRepository;
import com.cloud.userservice.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import static com.cloud.userservice.util.ErrorMessages.ENTITY_NOT_FOUND;
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public UserDetailDto getUserDetailByID(UUID id) {
        Optional<UserModel> optionalUserDetail = userRepository.findById(id);
        if (optionalUserDetail.isEmpty()) throw new CustomException(ENTITY_NOT_FOUND + id, HttpStatus.NOT_FOUND);
        return Converter.toDto(optionalUserDetail.get().getUserDetail());
    }
}
