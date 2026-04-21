package com.bit.backend.config;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    private boolean isInitialized = false;

    private void initializeTwilio() {
        if (!isInitialized) {
            Twilio.init(accountSid, authToken);
            isInitialized = true;
        }
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            initializeTwilio();

            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody)
                    .create();

            System.out.println("Message sent! SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            throw new RuntimeException("SMS sending failed: " + e.getMessage());
        }
    }

    public void sendAppointmentConfirmation(String customerPhone, String appointmentId,
            String appointmentDate, String appointmentTime) {
        String messageBody = String.format(
                "Your Appointment has been successfully created!%n" +
                        "Appointment ID: %s%n" +
                        "Appointment Date: %s%n" +
                        "Appointment Start Time: %s%n" +
                        "Thank you for choosing our service.",
                appointmentId, appointmentDate, appointmentTime);

        sendSms(customerPhone, messageBody);
    }
}
