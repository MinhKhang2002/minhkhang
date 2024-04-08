package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.CategoryDTO;
import com.laptrinhjavaweb.dto.UserDTO;
import com.laptrinhjavaweb.entity.CategoryEntity;
import com.laptrinhjavaweb.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        userDTO.setId(userEntity.getId());
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

    public List<UserDTO> toDtoList(List<UserEntity> entities) {
        // Khai báo một danh sách (List) để lưu trữ các đối tượng DTO
        List<UserDTO> dtos = new ArrayList<>();
        // Lặp qua từng đối tượng CategoryEntity trong danh sách entities
        for (UserEntity entity : entities) {
            // Thay thế để ngắn gọn hơn cách trên
            // Gọi phương thức toDTO để chuyển đổi từ CategoryEntity sang CategoryDTO
            // Thêm đối tượng DTO đã chuyển đổi vào danh sách dtos
            dtos.add(toDTO(entity));
        }
        return dtos;
    }
}
