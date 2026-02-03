package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.entities.EmployeeLeaveEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T20:00:35+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class EmployeeLeaveMapperImpl implements EmployeeLeaveMapper {

    @Override
    public EmployeeLeaveDto toEmployeeLeaveDto(EmployeeLeaveEntity employeeLeaveEntity) {
        if ( employeeLeaveEntity == null ) {
            return null;
        }

        String employeeName = null;
        String endDate = null;
        long id = 0L;
        String leaveType = null;
        String reason = null;
        String startDate = null;

        employeeName = employeeLeaveEntity.getEmployeeName();
        endDate = employeeLeaveEntity.getEndDate();
        if ( employeeLeaveEntity.getId() != null ) {
            id = employeeLeaveEntity.getId();
        }
        leaveType = employeeLeaveEntity.getLeaveType();
        reason = employeeLeaveEntity.getReason();
        startDate = employeeLeaveEntity.getStartDate();

        EmployeeLeaveDto employeeLeaveDto = new EmployeeLeaveDto( id, employeeName, leaveType, startDate, endDate, reason );

        return employeeLeaveDto;
    }

    @Override
    public EmployeeLeaveEntity toEmployeeLeaveEntity(EmployeeLeaveDto employeeLeaveDto) {
        if ( employeeLeaveDto == null ) {
            return null;
        }

        String employeeName = null;
        String endDate = null;
        String leaveType = null;
        String reason = null;
        String startDate = null;
        Long id = null;

        employeeName = employeeLeaveDto.getEmployeeName();
        endDate = employeeLeaveDto.getEndDate();
        leaveType = employeeLeaveDto.getLeaveType();
        reason = employeeLeaveDto.getReason();
        startDate = employeeLeaveDto.getStartDate();
        id = employeeLeaveDto.getId();

        EmployeeLeaveEntity employeeLeaveEntity = new EmployeeLeaveEntity( id, employeeName, leaveType, startDate, endDate, reason );

        return employeeLeaveEntity;
    }

    @Override
    public List<EmployeeLeaveDto> toEmployeeLeaveDtoList(List<EmployeeLeaveEntity> employeeLeaveEntityList) {
        if ( employeeLeaveEntityList == null ) {
            return null;
        }

        List<EmployeeLeaveDto> list = new ArrayList<EmployeeLeaveDto>( employeeLeaveEntityList.size() );
        for ( EmployeeLeaveEntity employeeLeaveEntity : employeeLeaveEntityList ) {
            list.add( toEmployeeLeaveDto( employeeLeaveEntity ) );
        }

        return list;
    }
}
