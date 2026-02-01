package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-01T12:27:52+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class EmployeeRegMapperImpl implements EmployeeRegMapper {

    @Override
    public EmployeeRegDto toEmployeeRegDto(EmployeeRegEntity employeeRegEntity) {
        if ( employeeRegEntity == null ) {
            return null;
        }

        EmployeeRegDto.EmployeeRegDtoBuilder employeeRegDto = EmployeeRegDto.builder();

        employeeRegDto.actions( employeeRegEntity.getActions() );
        employeeRegDto.commissionRate( employeeRegEntity.getCommissionRate() );
        employeeRegDto.dateJoined( employeeRegEntity.getDateJoined() );
        employeeRegDto.designation( employeeRegEntity.getDesignation() );
        employeeRegDto.employeeName( employeeRegEntity.getEmployeeName() );
        employeeRegDto.hourlyRate( employeeRegEntity.getHourlyRate() );
        employeeRegDto.id( employeeRegEntity.getId() );
        employeeRegDto.maxAppointmentsPerDay( employeeRegEntity.getMaxAppointmentsPerDay() );
        employeeRegDto.specializations( employeeRegEntity.getSpecializations() );
        employeeRegDto.weeklyOffDays( employeeRegEntity.getWeeklyOffDays() );

        return employeeRegDto.build();
    }

    @Override
    public EmployeeRegEntity toEmployeeRegEntity(EmployeeRegDto employeeRegDto) {
        if ( employeeRegDto == null ) {
            return null;
        }

        EmployeeRegEntity employeeRegEntity = new EmployeeRegEntity();

        employeeRegEntity.setActions( employeeRegDto.getActions() );
        employeeRegEntity.setCommissionRate( employeeRegDto.getCommissionRate() );
        employeeRegEntity.setDateJoined( employeeRegDto.getDateJoined() );
        employeeRegEntity.setDesignation( employeeRegDto.getDesignation() );
        employeeRegEntity.setEmployeeName( employeeRegDto.getEmployeeName() );
        employeeRegEntity.setHourlyRate( employeeRegDto.getHourlyRate() );
        employeeRegEntity.setMaxAppointmentsPerDay( employeeRegDto.getMaxAppointmentsPerDay() );
        employeeRegEntity.setSpecializations( employeeRegDto.getSpecializations() );
        employeeRegEntity.setWeeklyOffDays( employeeRegDto.getWeeklyOffDays() );
        if ( employeeRegDto.getId() != null ) {
            employeeRegEntity.setId( employeeRegDto.getId() );
        }

        return employeeRegEntity;
    }

    @Override
    public List<EmployeeRegDto> toEmployeeRegDtoList(List<EmployeeRegEntity> employeeRegEntityList) {
        if ( employeeRegEntityList == null ) {
            return null;
        }

        List<EmployeeRegDto> list = new ArrayList<EmployeeRegDto>( employeeRegEntityList.size() );
        for ( EmployeeRegEntity employeeRegEntity : employeeRegEntityList ) {
            list.add( toEmployeeRegDto( employeeRegEntity ) );
        }

        return list;
    }
}
