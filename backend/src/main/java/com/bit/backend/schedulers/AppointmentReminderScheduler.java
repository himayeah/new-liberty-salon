package com.bit.backend.schedulers;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import com.bit.backend.mappers.AppointmentScheduleMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;
import com.bit.backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentReminderScheduler {

    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final AppointmentScheduleMapper appointmentScheduleMapper;
    private final NotificationService notificationService;

    // Run every day at 10:00 AM (can be adjusted)
    // Format: "0 0 10 * * ?"

    // Spring Annotation to run method at a scheduled time. Runs at 10 AM every day
    @Scheduled(cron = "0 0 10 * * ?")

    // Spring Annotation to ensure transactional entity.
    // Treats al database operation under this method as one transaction
    // If any operation fails, all will be rolled back, Saves are managed properly

    @Transactional
    public void sendAppointmentReminders() {
        System.out.println("Checking for appointments tomorrow to send reminders...");

        List<AppointmentScheduleEntity> upcomingAppointments = appointmentScheduleRepository
                .findAppointmentsForReminder();

        System.out.println("Found " + upcomingAppointments.size() + " appointments for reminders.");

        for (AppointmentScheduleEntity entity : upcomingAppointments) {
            try {
                AppointmentScheduleDto dto = appointmentScheduleMapper.toAppointmentScheduleDto(entity);

                // Double check if client email is available
                if (entity.getClient() != null && entity.getClient().getEmail() != null) {

                    // Prepare email body using DTO
                    dto.setClientEmail(entity.getClient().getEmail());

                    notificationService.sendAppointmentReminder(dto);

                    // Mark as sent to prevent sending again tomorrow
                    entity.setReminderSent(true);
                    appointmentScheduleRepository.save(entity);
                }
            } catch (Exception e) {
                System.err.println(
                        "Failed to process reminder for appointment ID " + entity.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("Reminder processing completed.");
    }
}
