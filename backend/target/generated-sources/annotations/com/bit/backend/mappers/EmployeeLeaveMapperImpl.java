package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeLeaveDto;
import com.bit.backend.entities.EmployeeLeaveEntity;
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
public class EmployeeLeaveMapperImpl implements EmployeeLeaveMapper {

    @Override
    public EmployeeLeaveDto toEmployeeLeaveDto(EmployeeLeaveEntity employeeLeaveEntity) {
        if ( employeeLeaveEntity == null ) {
            return null;
        }

        long id = 0L;
        String employeeName = null;
        String leaveType = null;
        String startDate = null;
        String endDate = null;
        String reason = null;

        if ( employeeLeaveEntity.getId() != null ) {
            id = employeeLeaveEntity.getId();
        }
        employeeName = employeeLeaveEntity.getEmployeeName();
        leaveType = employeeLeaveEntity.getLeaveType();
        startDate = employeeLeaveEntity.getStartDate();
        endDate = employeeLeaveEntity.getEndDate();
        reason = employeeLeaveEntity.getReason();

        EmployeeLeaveDto employeeLeaveDto = new EmployeeLeaveDto( id, employeeName, leaveType, startDate, endDate, reason );

        return employeeLeaveDto;
    }

    @Override
    public EmployeeLeaveEntity toEmployeeLeaveEntity(EmployeeLeaveDto employeeLeaveDto) {
        if ( employeeLeaveDto == null ) {
            return null;
        }

        Long id = null;
        String employeeName = null;
        String leaveType = null;
        String startDate = null;
        String endDate = null;
        String reason = null;

        id = employeeLeaveDto.getId();
        employeeName = employeeLeaveDto.getEmployeeName();
        leaveType = employeeLeaveDto.getLeaveType();
        startDate = employeeLeaveDto.getStartDate();
        endDate = employeeLeaveDto.getEndDate();
        reason = employeeLeaveDto.getReason();

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
