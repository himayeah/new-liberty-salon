package com.bit.backend.mappers;

import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T23:06:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class EmployeeRegMapperImpl implements EmployeeRegMapper {

    @Override
    public EmployeeRegDto toEmployeeRegDto(EmployeeRegEntity employeeRegEntity) {
        if ( employeeRegEntity == null ) {
            return null;
        }

        EmployeeRegDto employeeRegDto = new EmployeeRegDto();

        return employeeRegDto;
    }

    @Override
    public EmployeeRegEntity toEmployeeRegEntity(EmployeeRegDto employeeRegDto) {
        if ( employeeRegDto == null ) {
            return null;
        }

        EmployeeRegEntity employeeRegEntity = new EmployeeRegEntity();

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
