package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.ReportAppointmentStatusDto;
import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;
import com.bit.backend.dtos.AppointmentScheduleDto;

public interface ReportAppointmentStatusService {

    long countCancelledAppointments();

    List<ReportCancelledAppointmentScheduleDto> getCancelledAppointmentDetails();

    List<ReportAppointmentStatusDto> getAppointmentsBySource();

    List<AppointmentScheduleDto> getAppointmentCountByStatus();

}
