package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.AppointmentScheduleDto;

public interface AppointmentScheduleServiceI {

    AppointmentScheduleDto addAppointment(AppointmentScheduleDto appointmentScheduleDto);

    List<AppointmentScheduleDto> getAppointments();

    AppointmentScheduleDto getAppointmentById(long id);

    AppointmentScheduleDto updateAppointment(long id, AppointmentScheduleDto appointmentScheduleDto);

    AppointmentScheduleDto deleteAppointment(long id);

    // dashboard card
    long countAppointmentsLast30Days();

    // dashboard card
    String getMostUsedService();

    List<Object[]> getAppointmentCountsByMonth();

    List<Object[]> getTop3Services();

    List<Object[]> getTop5Employees();

    List<AppointmentScheduleDto> getUpcomingNotifications();

    Long getMaxId();

}
