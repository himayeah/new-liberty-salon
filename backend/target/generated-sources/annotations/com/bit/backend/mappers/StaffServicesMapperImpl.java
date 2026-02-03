package com.bit.backend.mappers;

import com.bit.backend.dtos.StaffServicesDto;
import com.bit.backend.entities.StaffServicesEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T22:45:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class StaffServicesMapperImpl implements StaffServicesMapper {

    @Override
    public StaffServicesDto toStaffServicesDto(StaffServicesEntity staffServicesEntity) {
        if ( staffServicesEntity == null ) {
            return null;
        }

        StaffServicesDto staffServicesDto = new StaffServicesDto();

        staffServicesDto.setId( staffServicesEntity.getId() );
        staffServicesDto.setServiceName( staffServicesEntity.getServiceName() );

        return staffServicesDto;
    }

    @Override
    public StaffServicesEntity toStaffServicesEntity(StaffServicesDto staffServicesDto) {
        if ( staffServicesDto == null ) {
            return null;
        }

        StaffServicesEntity staffServicesEntity = new StaffServicesEntity();

        staffServicesEntity.setId( staffServicesDto.getId() );
        staffServicesEntity.setServiceName( staffServicesDto.getServiceName() );

        return staffServicesEntity;
    }

    @Override
    public List<StaffServicesDto> toStaffServicesDtoList(List<StaffServicesEntity> staffServicesEntityList) {
        if ( staffServicesEntityList == null ) {
            return null;
        }

        List<StaffServicesDto> list = new ArrayList<StaffServicesDto>( staffServicesEntityList.size() );
        for ( StaffServicesEntity staffServicesEntity : staffServicesEntityList ) {
            list.add( toStaffServicesDto( staffServicesEntity ) );
        }

        return list;
    }
}
