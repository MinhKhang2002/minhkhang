package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.converter.RoleConverter;
import com.laptrinhjavaweb.dto.RoleDTO;
import com.laptrinhjavaweb.entity.RoleEntity;
import com.laptrinhjavaweb.repository.RoleRepository;
import com.laptrinhjavaweb.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleConverter roleConverter;

    @Override
    public List<RoleDTO> getAllRole() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleConverter.toDtoList(roleEntities);
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setCode(roleDTO.getCode());
        roleEntity.setName(roleDTO.getName());
        roleEntity.setCategories(roleDTO.getCategories());

        RoleEntity savedRole = roleRepository.save(roleEntity);
        return roleConverter.toDTO(savedRole); // sử dụng converter để chuyển đổi
    }
}
