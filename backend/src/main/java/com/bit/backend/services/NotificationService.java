package com.bit.backend.services;

import com.bit.backend.dtos.AppointmentScheduleDto;

public interface NotificationService {
    void sendAppointmentNotification(AppointmentScheduleDto appointment);
    void sendAppointmentReminder(AppointmentScheduleDto appointment);
}
