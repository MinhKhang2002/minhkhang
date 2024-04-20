package com.laptrinhjavaweb.service;

import com.laptrinhjavaweb.dto.RoleDTO;

import java.util.List;

public interface IRoleService {
    List<RoleDTO> getAllRole();

    RoleDTO addRole(RoleDTO roleDTO);
}
