package com.bit.backend.services;

import com.bit.backend.dtos.StaffServicesDto;

import java.util.List;

public interface StaffServicesServiceI {
    StaffServicesDto createStaffServices(StaffServicesDto staffServicesDto);
    List<StaffServicesDto> getStaffServices();
    StaffServicesDto updateStaffServices(long id, StaffServicesDto staffServicesDto);
    StaffServicesDto deleteStaffServices(long id);
}
