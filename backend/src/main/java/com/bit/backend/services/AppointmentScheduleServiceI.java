package com.bit.backend.services;

import com.bit.backend.dtos.AppointmentScheduleDto;

import java.util.List;

public interface AppointmentScheduleServiceI {

    AppointmentScheduleDto addAppointment(AppointmentScheduleDto appointmentScheduleDto);

    List<AppointmentScheduleDto> getAppointment();

    AppointmentScheduleDto updateAppointment(long id, AppointmentScheduleDto appointmentScheduleDto);

    AppointmentScheduleDto deleteAppointment(long id);

    List<AppointmentScheduleDto> getAppointments();

    // dashboard card
    long countAppointmentsLast30Days();

    // dashboard card
    String getMostUsedService();

    List<Object[]> getAppointmentCountsByMonth();

    List<Object[]> getTop3Services();
    
    List<Object[]> getTop5Employees();

}
