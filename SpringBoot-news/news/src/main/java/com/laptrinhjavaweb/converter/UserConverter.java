package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setStatus(userDTO.getStatus());
        // Chuyển đổi các trường khác

        return userEntity;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setFullName(userEntity.getFullName());
        userDTO.setStatus(userEntity.getStatus());
        // Chuyển đổi các trường khác

        return userDTO;
    }
}
