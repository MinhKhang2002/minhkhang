package com.laptrinhjavaweb.api;

import com.laptrinhjavaweb.api.output.RoleOutput;
import com.laptrinhjavaweb.dto.RoleDTO;
import com.laptrinhjavaweb.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@CrossOrigin
public class RoleAPI {

    @Autowired
    private RoleService roleService;

    @GetMapping("/allRole")
    public ResponseEntity<List<RoleDTO>> showALlRole() {
        List<RoleDTO> roleDTOList = roleService.getAllRole();
        return new ResponseEntity<>(roleDTOList, HttpStatus.OK);
    }
}
