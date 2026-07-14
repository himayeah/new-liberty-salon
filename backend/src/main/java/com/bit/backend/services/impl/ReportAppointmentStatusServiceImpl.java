package com.bit.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.ReportAppointmentStatusDto;
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

    // appointment cancallation table
    @Override
    public List<ReportAppointmentStatusDto> getCancelledAppointmentDetails() {

        List<Object[]> rows = appointmentScheduleRepository.getCancelledAppointmentDetails();

        // create an empty array List
        List<ReportAppointmentStatusDto> list = new ArrayList<>();

        for (Object[] row : rows) {

            // creates a new Dto, as the for loop iterates through earch row, data is mapped to the Dto and added to the list.
            // If you don't create a new Dto object inside the loop, the same object will be updated and added to the list multiple times,
            // resulting in a list with duplicate references to the same object.
            ReportAppointmentStatusDto dto = new ReportAppointmentStatusDto();

            dto.setClientName((String) row[0]);
            dto.setServiceName((String) row[1]);
            dto.setCancelledDate((String) row[2]);
            dto.setCancellationReason((String) row[3]);
            list.add(dto);

        }
        
        return list;

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

   @Override
   public List<ReportAppointmentStatusDto> getAppointmentCountByStatus() {
    try {
        List<Object[]> rows = appointmentScheduleRepository.getAppointmentCountByStatus();

        // Create an empty array List
        List<ReportAppointmentStatusDto> list = new ArrayList<>();

        for (Object[] row : rows) {

            // creates a new Dto, as the for loop iterates through earch row, data is mapped to the Dto and added to the list.
            // If you don't create a new Dto object inside the loop, the same object will be updated and added to the list multiple times,
            // resulting in a list with duplicate references to the same object.
            ReportAppointmentStatusDto reportAppointmentStatusDto = new ReportAppointmentStatusDto();

            reportAppointmentStatusDto.setAppointmentStatus((String) row[0]);
            reportAppointmentStatusDto.setAppointmentCount(((Number) row[1]).longValue());

            // the final dto is added to the list
            list.add(reportAppointmentStatusDto);
        }

        return list;

    } catch (Exception e) {
        throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    // top 3 services (pie chart)
    @Override
    public List<ReportAppointmentStatusDto> getTop3Services() {
        try {
            List<Object[]> rows = appointmentScheduleRepository.getTop3Services();

            // Create an empty array List
            List<ReportAppointmentStatusDto> list = new ArrayList<>();

            for (Object[] row : rows) {

                // creates a new Dto, as the for loop iterates through earch row, data is mapped to the Dto and added to the list.
                // If you don't create a new Dto object inside the loop, the same object will be updated and added to the list multiple times,
                // resulting in a list with duplicate references to the same object.
                ReportAppointmentStatusDto reportAppointmentStatusDto = new ReportAppointmentStatusDto();

                reportAppointmentStatusDto.setTopServiceName((String) row[0]);
                reportAppointmentStatusDto.setTotalAppointmentCount(((Number) row[1]).longValue());

                // the final dto is added to the list
                list.add(reportAppointmentStatusDto);
            }

            return list;

        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }






}








 

}
