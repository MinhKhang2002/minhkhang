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
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setStatus(userDTO.getStatus());
        userEntity.setCreatedDate(userDTO.getCreatedDate());
        userEntity.setModifiedDate(userDTO.getModifiedDate());
        userEntity.setCreatedBy(userDTO.getCreatedBy());
        userEntity.setModifiedBy(userDTO.getModifiedBy());
        return userEntity;
    }

    public UserDTO toDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setFullName(userEntity.getFullName());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setCreatedBy(userEntity.getCreatedBy());
        userDTO.setCreatedDate(userEntity.getCreatedDate());
        userDTO.setModifiedBy(userEntity.getModifiedBy());
        userDTO.setCreatedDate(userEntity.getCreatedDate());
        return userDTO;
    }
}
