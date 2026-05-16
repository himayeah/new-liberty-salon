package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.ReportAppointmentStatusDto;
import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;

public interface ReportAppointmentStatusService {

    long countCancelledAppointments();

    List<ReportCancelledAppointmentScheduleDto> getCancelledAppointmentDetails();

    List<ReportAppointmentStatusDto> getAppointmentsBySource();

}
