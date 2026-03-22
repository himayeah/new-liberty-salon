package com.bit.backend.mappers;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AppointmentScheduleMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(target = "clientName", expression = "java(appointmentScheduleEntity.getClient() != null ? appointmentScheduleEntity.getClient().getFirstName() + \" \" + (appointmentScheduleEntity.getClient().getLastName() != null ? appointmentScheduleEntity.getClient().getLastName() : \"\") : null)")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.employeeName", target = "employeeName")
    @Mapping(source = "service.id", target = "serviceId")
    @Mapping(source = "service.serviceName", target = "serviceName")
    AppointmentScheduleDto toAppointmentScheduleDto(AppointmentScheduleEntity appointmentScheduleEntity);

    AppointmentScheduleEntity toAppointmentScheduleEntity(AppointmentScheduleDto appointmentScheduleDto);

    List<AppointmentScheduleDto> toAppointmentScheduleDtoList(
            List<AppointmentScheduleEntity> appointmentScheduleEntityList);

}
