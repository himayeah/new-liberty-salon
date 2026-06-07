package com.bit.backend.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.dtos.PrivilegeGroupDto;
import com.bit.backend.services.PrivilegeGroupServiceI;

@RestController
public class PrivilegeGroupController {

    private final PrivilegeGroupServiceI privilegeGroupServiceI;

    public PrivilegeGroupController(PrivilegeGroupServiceI privilegeGroupServiceI) {
        this.privilegeGroupServiceI = privilegeGroupServiceI;
    }

    @GetMapping("/privilege-groups")
    public ResponseEntity<List<PrivilegeGroupDto>> getPrivilegeGroups() {
        List<PrivilegeGroupDto> privilegeGroupDtos = privilegeGroupServiceI.getPrivilegeGroups();
        return ResponseEntity.ok(privilegeGroupDtos);
    }

    @PostMapping("/privilege-groups")
    public ResponseEntity<PrivilegeGroupDto> addPrivilegeGroup(@RequestBody PrivilegeGroupDto privilegeGroupDto) {
        PrivilegeGroupDto privilegeGroupDtoResponse = privilegeGroupServiceI.addPrivilegeGroup(privilegeGroupDto);
        return ResponseEntity.created(URI.create("/privilege-groups/" + privilegeGroupDtoResponse.getId())).body(privilegeGroupDtoResponse);
    }

    @PutMapping("/privilege-groups/{id}")
    public ResponseEntity<PrivilegeGroupDto> updatePrivilegeGroup(@PathVariable long id, @RequestBody PrivilegeGroupDto privilegeGroupDto) {
        return ResponseEntity.ok(privilegeGroupServiceI.updatePrivilegeGroup(id, privilegeGroupDto));
    }

    @PutMapping("/privilege-groups/delete/{id}")
    public ResponseEntity<PrivilegeGroupDto> deletePrivilegeGroup(@PathVariable long id, @RequestBody PrivilegeGroupDto privilegeGroupDto) {
        return ResponseEntity.ok(privilegeGroupServiceI.deletePrivilegeGroup(id, privilegeGroupDto));
    }
}
