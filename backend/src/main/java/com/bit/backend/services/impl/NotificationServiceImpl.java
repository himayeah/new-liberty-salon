package com.bit.backend.services.impl;

import com.bit.backend.config.EmailSender;
import com.bit.backend.config.SmsSender;
import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final EmailSender emailSender;
    private final SmsSender smsSender;

    @Override
    public void sendAppointmentNotification(AppointmentScheduleDto appointment) {
        try {
            // Determine recipient and booking type
            // If it's an Internal booking, by default goes to receptionist's email and if
            // it's a public booking it goes to the public email
            String recipient = "receptionist@newlibertysalon.com";
            String bookingType = "Internal Booking";

            if ("PUBLIC_WEB".equalsIgnoreCase(appointment.getBookingSource())) {
                recipient = "libertysalontest@gmail.com";
                bookingType = "Public Online Booking";
            }

            // Create email subject and body
            String clientPhone = appointment.getClientPhone() != null ? appointment.getClientPhone() : "N/A";
            String subject = String.format("New Appointment: %s - %s (%s) [%s]",
                    appointment.getServiceName(),
                    appointment.getClientName(),
                    clientPhone,
                    bookingType);

            // calls the method constructEmailBody to create the email body
            String body = constructEmailBody(appointment, bookingType);

            emailSender.sendSimpleEmail(recipient, subject, body);

            // Send SMS to client if phone number is available
            if (appointment.getClientPhone() != null && !appointment.getClientPhone().isEmpty()) {
                smsSender.sendAppointmentConfirmation(
                        appointment.getClientPhone(),
                        String.valueOf(appointment.getId()),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentStartTime());
            }
        } catch (Exception e) {
            // Log error but don't disrupt the main flow
            System.err.println("Notification failed: " + e.getMessage());
        }
    }

    @Override
    public void sendAppointmentReminder(AppointmentScheduleDto appointment) {
        try {

            // Is email null? meaning doesn't exist at all. or is email empty? meaning
            // the client might have provided an empty email
            if (appointment.getClientEmail() == null || appointment.getClientEmail().isEmpty()) {
                System.out.println("No email address found for cli  ent, skipping reminder.");
                return;
            }

            String subject = "Reminder: Your Appointment at New Liberty Salon tomorrow!";
            String body = String.format(
                    "Hello %s,%n%n" +
                            "This is a friendly reminder for your upcoming appointment tomorrow.%n%n" +
                            "Appointment Details:%n" +
                            "-------------------%n" +
                            "Service:           %s%n" +
                            "Date:              %s%n" +
                            "Time:              %s%n%n" +
                            "We look forward to seeing you!%n%n" +
                            "Best Regards,%n" +
                            "New Liberty Salon Team",
                    appointment.getClientName(),
                    appointment.getServiceName(),
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentStartTime());

            // contains subject, body etc
            emailSender.sendSimpleEmail(appointment.getClientEmail(), subject, body);
            // System notification in console mentioning that the reminder has been sent to
            // the client email
            System.out.println("Reminder email sent to: " + appointment.getClientEmail());
        } catch (Exception e) {
            System.err.println("Failed to send reminder: " + e.getMessage());
        }
    }

    private String constructEmailBody(AppointmentScheduleDto appointment, String bookingType) {
        return String.format(
                "Hello,%n%n" +
                        "A new appointment has been scheduled through the %s.%n%n" +
                        "Appointment Details:%n" +
                        "-------------------%n" +
                        "Client Name:       %s%n" +
                        "Client Phone:      %s%n" +
                        "Service:           %s%n" +
                        "Stylist:           %s%n" +
                        "Date:              %s%n" +
                        "Start Time:        %s%n" +
                        "Notes:             %s%n%n" +
                        "Please log in to the system for more details.%n%n" +
                        "Best Regards,%n" +
                        "New Liberty Salon Notification System",
                bookingType.toLowerCase(),
                appointment.getClientName(),
                appointment.getClientPhone(),
                appointment.getServiceName(),
                appointment.getEmployeeName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentStartTime(),
                appointment.getNotes() != null ? appointment.getNotes() : "N/A");
    }
}
