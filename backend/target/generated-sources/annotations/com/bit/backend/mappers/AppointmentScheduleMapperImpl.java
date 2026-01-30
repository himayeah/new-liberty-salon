package com.bit.backend.mappers;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-30T22:58:52+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AppointmentScheduleMapperImpl implements AppointmentScheduleMapper {

    @Override
    public AppointmentScheduleEntity toAppointmentScheduleEntity(AppointmentScheduleDto appointmentScheduleDto) {
        if ( appointmentScheduleDto == null ) {
            return null;
        }

        Long id = null;
        String clientName = null;
        String stylistName = null;
        String serviceName = null;
        String appointmentDate = null;
        String appointmentStartTime = null;
        String appointmentEndTime = null;
        String appointmentStatus = null;
        String bookingSource = null;
        String notes = null;
        String cancellationReason = null;

        id = appointmentScheduleDto.getId();
        clientName = appointmentScheduleDto.getClientName();
        stylistName = appointmentScheduleDto.getStylistName();
        serviceName = appointmentScheduleDto.getServiceName();
        appointmentDate = appointmentScheduleDto.getAppointmentDate();
        appointmentStartTime = appointmentScheduleDto.getAppointmentStartTime();
        appointmentEndTime = appointmentScheduleDto.getAppointmentEndTime();
        appointmentStatus = appointmentScheduleDto.getAppointmentStatus();
        bookingSource = appointmentScheduleDto.getBookingSource();
        notes = appointmentScheduleDto.getNotes();
        cancellationReason = appointmentScheduleDto.getCancellationReason();

        AppointmentScheduleEntity appointmentScheduleEntity = new AppointmentScheduleEntity( id, clientName, stylistName, serviceName, appointmentDate, appointmentStartTime, appointmentEndTime, appointmentStatus, bookingSource, notes, cancellationReason );

        return appointmentScheduleEntity;
    }

    @Override
    public AppointmentScheduleDto toAppointmentScheduleDto(AppointmentScheduleEntity appointmentScheduleEntity) {
        if ( appointmentScheduleEntity == null ) {
            return null;
        }

        long id = 0L;
        String clientName = null;
        String stylistName = null;
        String serviceName = null;
        String appointmentDate = null;
        String appointmentStartTime = null;
        String appointmentEndTime = null;
        String appointmentStatus = null;
        String bookingSource = null;
        String notes = null;
        String cancellationReason = null;

        AppointmentScheduleDto appointmentScheduleDto = new AppointmentScheduleDto( id, clientName, stylistName, serviceName, appointmentDate, appointmentStartTime, appointmentEndTime, appointmentStatus, bookingSource, notes, cancellationReason );

        return appointmentScheduleDto;
    }

    @Override
    public List<AppointmentScheduleDto> toAppointmentScheduleDtoList(List<AppointmentScheduleEntity> appointmentScheduleEntityList) {
        if ( appointmentScheduleEntityList == null ) {
            return null;
        }

        List<AppointmentScheduleDto> list = new ArrayList<AppointmentScheduleDto>( appointmentScheduleEntityList.size() );
        for ( AppointmentScheduleEntity appointmentScheduleEntity : appointmentScheduleEntityList ) {
            list.add( toAppointmentScheduleDto( appointmentScheduleEntity ) );
        }

        return list;
    }
}
