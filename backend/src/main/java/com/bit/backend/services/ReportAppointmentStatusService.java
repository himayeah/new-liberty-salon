package com.bit.backend.services;

import com.bit.backend.dtos.ReportCancelledAppointmentScheduleDto;

import java.util.List;

public interface ReportAppointmentStatusService {

    // count cancelled appointments

    // long<AppointmentScheduleDto> countCancelledAppointments(); --> wrong. long is
    // a primitive type
    // and you can't use <> with these. other primitives:

    long countCancelledAppointments();

    List<ReportCancelledAppointmentScheduleDto> getCancelledAppointmentDetails();

}
