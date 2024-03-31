package com.laptrinhjavaweb.converter;

import com.laptrinhjavaweb.dto.RoleDTO;
import com.laptrinhjavaweb.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleConverter {
    /*public RoleEntity toEntity(RoleDTO roleDTO) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.
    }*/

    public RoleDTO toDTO(RoleEntity roleEntity) {
         RoleDTO roleDTO = new RoleDTO();
         roleDTO.setId(roleEntity.getId());
         roleDTO.setCode(roleEntity.getCode());
         roleDTO.setName(roleEntity.getName());
         roleDTO.setCategories(roleEntity.getCategories());
         return roleDTO;
    }

    public List<RoleDTO> toDtoList(List<RoleEntity> roleEntityList){
        List<RoleDTO> dtos = new ArrayList<>();

        for(RoleEntity roleEntity: roleEntityList) {
            dtos.add(toDTO(roleEntity));
        }
        return dtos;
    }
}
