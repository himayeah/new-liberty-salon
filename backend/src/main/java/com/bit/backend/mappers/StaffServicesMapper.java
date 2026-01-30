package com.bit.backend.mappers;

import com.bit.backend.dtos.StaffServicesDto;
import com.bit.backend.entities.StaffServicesEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffServicesMapper {
    StaffServicesDto toStaffServicesDto(StaffServicesEntity staffServicesEntity);
    StaffServicesEntity toStaffServicesEntity(StaffServicesDto staffServicesDto);
    List<StaffServicesDto> toStaffServicesDtoList(List<StaffServicesEntity> staffServicesEntityList);

}
