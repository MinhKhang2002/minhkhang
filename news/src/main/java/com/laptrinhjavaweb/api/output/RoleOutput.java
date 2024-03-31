package com.laptrinhjavaweb.api.output;

import com.laptrinhjavaweb.dto.NewDTO;
import com.laptrinhjavaweb.dto.RoleDTO;

import java.util.ArrayList;
import java.util.List;

public class RoleOutput {
    private List<RoleDTO> listResult = new ArrayList<>();

    public List<RoleDTO> getListResult() {
        return listResult;
    }

    public void setListResult(List<RoleDTO> listResult) {
        this.listResult = listResult;
    }
}
