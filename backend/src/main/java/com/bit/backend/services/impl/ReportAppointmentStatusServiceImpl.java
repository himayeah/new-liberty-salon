package com.bit.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.dtos.ReportAppointmentStatusDto;
import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.AppointmentScheduleMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;
import com.bit.backend.services.ReportAppointmentStatusService;

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
    public List<ReportCancelledAppointmentScheduleDto> getCancelledAppointmentDetails() {
        List<Object[]> results = appointmentScheduleRepository.getCancelledAppointmentDetails();
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

    // Report- getBookingsBySource (Pie Chart)
    @Override
    public List<ReportAppointmentStatusDto> getAppointmentsBySource() {
        try {
            return appointmentScheduleRepository.getAppointmentsBySource();
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Appointment Count By Status (bar chart) --------> ******HIMAYA- You only have
    // to manually map if you are returning Repo query in a List<Object[]>, for
    // @Override
    // public List<AppointmentScheduleDto> getAppointmentCountByStatus() {
    // try {

    // List<AppointmentScheduleDto> results =
    // appointmentScheduleRepository.getAppointmentCountByStatus();

    // Accessing Object Array elements
    // for (Object[] row : results) {
    // System.out.println("Age Group: " + row[0]);
    // System.out.println("----------------------");
    // System.out.println("Total Client Count: " + row[1]);
    // }

    // return results.stream()
    // .map(obj -> {
    // Object[] array = (Object[]) obj;
    // AppointmentScheduleDto dto = new AppointmentScheduleDto();
    // dto.setAppointmentStatus(array[0] != null ? array[0].toString() : null);
    // dto.totalCount(((Number) array[1]).longValue());
    // System.out.println("Registrations By Age Group:" + dto);
    // return dto;
    // })
    // .collect(Collectors.toList());
    // } catch (Exception e) {
    // System.err.println("Error fetching registration data: " + e.getMessage());
    // e.printStackTrace();
    // throw new com.bit.backend.exceptions.AppException("Error fetching
    // registration data",
    // org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }



   @Override
   public List<AppointmentScheduleDto> getAppointmentCountByStatus() {
    try {
        List<Object[]> rows = appointmentScheduleRepository.getAppointmentCountByStatus();

        List<AppointmentScheduleDto> list = new ArrayList<>();

        for (Object[] row : rows) {

            AppointmentScheduleDto appointmentScheduleDto = new AppointmentScheduleDto();

            appointmentScheduleDto.setAppointmentStatus((String) row[0]);
            appointmentScheduleDto.setTotalCount(((Number) row[1]).longValue());

            list.add(appointmentScheduleDto);
        }

        return list;

    } catch (Exception e) {
        throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}








 

}
