package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.ReportAppointmentStatusDto;

public interface ReportAppointmentStatusService {

    long countCancelledAppointments();

    List<ReportAppointmentStatusDto> getCancelledAppointmentDetails();

    List<ReportAppointmentStatusDto> getAppointmentsBySource();

    List<ReportAppointmentStatusDto> getAppointmentCountByStatus();

}
