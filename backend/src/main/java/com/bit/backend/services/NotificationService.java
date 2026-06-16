package com.bit.backend.services;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;

public interface NotificationService {
    void sendAppointmentNotification(AppointmentScheduleDto appointment);

    void sendAppointmentReminder(AppointmentScheduleDto appointment);

    void sendStylistCheckInNotification(AppointmentScheduleDto appointmentShScheduleDto);

}
