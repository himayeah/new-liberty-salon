package com.bit.backend.services.impl;

import com.bit.backend.services.ReportAppointmentStatusService;
import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;
import com.bit.backend.mappers.AppointmentScheduleMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReportAppointmentStatusServiceImpl implements ReportAppointmentStatusService {

    private final AppointmentScheduleRepository appointmentScheduleRepository;
    // private final AppointmentScheduleMapper appointmentScheduleMapper;

    public ReportAppointmentStatusServiceImpl(AppointmentScheduleRepository appointmentScheduleRepository,
            AppointmentScheduleMapper appointmentScheduleMapper) {
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        // this.appointmentScheduleMapper = appointmentScheduleMapper;
    }

    // Report- count cancelled appointments
    @Override
    public long countCancelledAppointments() {
        try {
            return appointmentScheduleRepository.countCancelledAppointments();
        } catch (Exception e) {
            throw new com.bit.backend.exceptions.AppException("Request failed with error: " + e.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Client Name | Service Name | Cancelled Date | Cancelled Reason of Cancelled
    // Appointments within last 3 Months
    @Override
    // the method will return a DTO List. The first getCancelledAppointmentDetails()
    // is called by the controller
    public List<ReportCancelledAppointmentScheduleDto> getCancelledAppointmentDetails() {

        // No need to create a new entity. call the method inside repository, and save
        // the returned Lst of Objects as "results"
        // The second getCancelledAppointmentDetails runs the sql query and returns a
        // DTO LIST, the first getCancelledAppointmentDetais() sends those DTO list back
        // to Controller
        List<Object[]> results = appointmentScheduleRepository.getCancelledAppointmentDetails();

        // Create an empty List that will store many
        // ReportCancelledAppointmentScheduleDto Objects
        List<ReportCancelledAppointmentScheduleDto> dtoList = new ArrayList<>();

        for (Object[] row : results) {

            // inside loop → create fresh object for each row
            ReportCancelledAppointmentScheduleDto dto = new ReportCancelledAppointmentScheduleDto();

            dto.setClientName(row[0] != null ? row[0].toString() : null);
            dto.setServiceName(row[1] != null ? row[1].toString() : null);
            dto.setCancelledDate(row[2] != null ? row[2].toString() : null);
            dto.setCancellationReason(row[3] != null ? row[3].toString() : null);

            dtoList.add(dto);

        }

        return dtoList;

    }

}
