package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T21:53:24+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class EmployeeRegMapperImpl implements EmployeeRegMapper {

    @Override
    public EmployeeRegDto toEmployeeRegDto(EmployeeRegEntity employeeRegEntity) {
        if ( employeeRegEntity == null ) {
            return null;
        }

        EmployeeRegDto.EmployeeRegDtoBuilder employeeRegDto = EmployeeRegDto.builder();

        employeeRegDto.id( employeeRegEntity.getId() );
        employeeRegDto.employeeName( employeeRegEntity.getEmployeeName() );
        employeeRegDto.dateJoined( employeeRegEntity.getDateJoined() );
        employeeRegDto.designation( employeeRegEntity.getDesignation() );
        employeeRegDto.specializations( employeeRegEntity.getSpecializations() );
        employeeRegDto.hourlyRate( employeeRegEntity.getHourlyRate() );
        employeeRegDto.commissionRate( employeeRegEntity.getCommissionRate() );
        employeeRegDto.weeklyOffDays( employeeRegEntity.getWeeklyOffDays() );
        employeeRegDto.maxAppointmentsPerDay( employeeRegEntity.getMaxAppointmentsPerDay() );
        employeeRegDto.actions( employeeRegEntity.getActions() );

        return employeeRegDto.build();
    }

    @Override
    public EmployeeRegEntity toEmployeeRegEntity(EmployeeRegDto employeeRegDto) {
        if ( employeeRegDto == null ) {
            return null;
        }

        EmployeeRegEntity employeeRegEntity = new EmployeeRegEntity();

        if ( employeeRegDto.getId() != null ) {
            employeeRegEntity.setId( employeeRegDto.getId() );
        }
        employeeRegEntity.setEmployeeName( employeeRegDto.getEmployeeName() );
        employeeRegEntity.setDateJoined( employeeRegDto.getDateJoined() );
        employeeRegEntity.setDesignation( employeeRegDto.getDesignation() );
        employeeRegEntity.setSpecializations( employeeRegDto.getSpecializations() );
        employeeRegEntity.setHourlyRate( employeeRegDto.getHourlyRate() );
        employeeRegEntity.setCommissionRate( employeeRegDto.getCommissionRate() );
        employeeRegEntity.setWeeklyOffDays( employeeRegDto.getWeeklyOffDays() );
        employeeRegEntity.setMaxAppointmentsPerDay( employeeRegDto.getMaxAppointmentsPerDay() );
        employeeRegEntity.setActions( employeeRegDto.getActions() );

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
