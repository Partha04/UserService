package com.cloud.userservice.util;

import com.cloud.userservice.dto.UserDetailDto;
import com.cloud.userservice.model.UserDetail;
import org.modelmapper.ModelMapper;

public class Converter {
    static ModelMapper mapper=new ModelMapper();

    public static UserDetail fromDto(UserDetailDto userDetailDto) {
        return mapper.map(userDetailDto, UserDetail.class);
    }

    public static UserDetailDto toDto(UserDetail userDetail) {
        return mapper.map(userDetail,UserDetailDto.class);
    }
}
