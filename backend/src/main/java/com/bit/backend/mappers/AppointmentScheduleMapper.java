package com.bit.backend.mappers;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentScheduleMapper {

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "service", ignore = true)
    AppointmentScheduleEntity toAppointmentScheduleEntity(AppointmentScheduleDto appointmentScheduleDto);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "clientName", expression = "java(appointmentScheduleEntity.getClient() != null ? appointmentScheduleEntity.getClient().getFirstName() + \" \" + appointmentScheduleEntity.getClient().getLastName() : null)")
    @Mapping(target = "employeeName", source = "employee.employeeName")
    @Mapping(target = "serviceName", source = "service.serviceName")
    AppointmentScheduleDto toAppointmentScheduleDto(AppointmentScheduleEntity appointmentScheduleEntity);

    List<AppointmentScheduleDto> toAppointmentScheduleDtoList(
            List<AppointmentScheduleEntity> appointmentScheduleEntityList);
}
