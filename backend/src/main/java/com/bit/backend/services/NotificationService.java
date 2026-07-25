package com.bit.backend.services;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;

public interface NotificationService {
    void sendAppointmentNotification(AppointmentScheduleDto appointment);

    void sendAppointmentReminder(AppointmentScheduleDto appointment);

    void sendStylistCheckInNotification(AppointmentScheduleDto appointmentShScheduleDto);

    // EMAIL step 1
    // Booking confirmation sent to client
    void sendAppointmentConfirmation(AppointmentScheduleDto appointmentShScheduleDto);

}
