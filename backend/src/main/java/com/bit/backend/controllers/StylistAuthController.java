package com.bit.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bit.backend.config.EmailSender;
import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.dtos.StylistLoginDto;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeRegMapper;
import com.bit.backend.repositories.EmployeeRegRepository;

@RestController
@RequestMapping("/api/v1/stylist-workspace")
public class StylistAuthController {

    private final EmployeeRegRepository employeeRegRepository;
    private final EmployeeRegMapper employeeRegMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    public StylistAuthController(EmployeeRegRepository employeeRegRepository,
                                 EmployeeRegMapper employeeRegMapper,
                                 PasswordEncoder passwordEncoder,
                                 EmailSender emailSender) {
        this.employeeRegRepository = employeeRegRepository;
        this.employeeRegMapper = employeeRegMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }

    /**
     * Authenticates a stylist using email and password.
     * Only employees with a set password are allowed to log in.
     *
     * @param loginDto contains email and password
     * @return the employee data if authenticated, or an error response
     */
    @PostMapping("/login")
    public ResponseEntity<EmployeeRegDto> stylistLogin(@RequestBody StylistLoginDto loginDto) {
        List<EmployeeRegEntity> employees = employeeRegRepository.findAllByEmail(loginDto.getEmail());
        if (employees.isEmpty()) {
            throw new AppException(
                    "No employee found with the provided email address.", HttpStatus.NOT_FOUND);
        }

        // Prioritize the employee that has a password set (completed the invite flow)
        EmployeeRegEntity employee = null;
        if (employees.size() == 1) {
            employee = employees.get(0);
        } else {
            for (EmployeeRegEntity emp : employees) {
                if (emp.getPassword() != null && !emp.getPassword().isEmpty()) {
                    employee = emp;
                    break;
                }
            }
            if (employee == null) {
                employee = employees.get(0);
            }
        }

        // Verify the employee has set a password (completed the invite flow)
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            throw new AppException(
                    "Your account is not yet activated. Please check your email for the invite link.",
                    HttpStatus.UNAUTHORIZED);
        }

        // Verify password using BCrypt or a legacy plain-text password stored previously
        if (!isPasswordValid(loginDto.getPassword(), employee.getPassword())) {
            throw new AppException("Incorrect password. Please try again.", HttpStatus.UNAUTHORIZED);
        }

        EmployeeRegDto dto = employeeRegMapper.toEmployeeRegDto(employee);
        return ResponseEntity.ok(dto);
    }

    /**
     * Sets the initial password for a newly invited employee.
     * Validates the invite token, then hashes and stores the password.
     *
     * @param payload contains token, email, and password
     * @return success message
     */
    @PostMapping("/set-password")
    public ResponseEntity<Map<String, String>> setPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String email = payload.get("email");
        String password = payload.get("password");

        if (token == null || email == null || password == null) {
            throw new AppException("Token, email, and password are required.", HttpStatus.BAD_REQUEST);
        }

        EmployeeRegEntity employee = employeeRegRepository
                .findByInviteToken(token)
                .orElseThrow(() -> new AppException(
                        "Invalid or expired invite link. Please contact your administrator.",
                        HttpStatus.BAD_REQUEST));

        // Ensure the email matches the employee record
        if (!employee.getEmail().equalsIgnoreCase(email)) {
            throw new AppException(
                    "The email address does not match the invite. Please use the email registered by your administrator.",
                    HttpStatus.BAD_REQUEST);
        }

        // Hash and save the password, clear the invite token
        employee.setPassword(passwordEncoder.encode(password));
        employee.setInviteToken(null);
        employeeRegRepository.save(employee);

        return ResponseEntity.ok(Map.of("message", "Password set successfully. You can now log in."));
    }

    /**
     * Initiates the password reset flow by generating a reset token
     * and sending a reset link to the employee's email.
     *
     * @param payload contains email
     * @return success message
     */
    private boolean isPasswordValid(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        if (storedPassword.equals(rawPassword)) {
            return true;
        }

        return passwordEncoder.matches(rawPassword, storedPassword);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.trim().isEmpty()) {
            throw new AppException("Email address is required.", HttpStatus.BAD_REQUEST);
        }

        List<EmployeeRegEntity> employees = employeeRegRepository.findAllByEmail(email);
        if (employees.isEmpty()) {
            throw new AppException(
                    "No employee found with the provided email address.", HttpStatus.NOT_FOUND);
        }

        EmployeeRegEntity employee = null;
        if (employees.size() == 1) {
            employee = employees.get(0);
        } else {
            for (EmployeeRegEntity emp : employees) {
                if (emp.getPassword() != null && !emp.getPassword().isEmpty()) {
                    employee = emp;
                    break;
                }
            }
            if (employee == null) {
                employee = employees.get(0);
            }
        }

        // Generate a reset token and save it
        String resetToken = UUID.randomUUID().toString();
        employee.setResetToken(resetToken);
        employeeRegRepository.save(employee);

        // Send the reset email
        String resetLink = "http://localhost:4200/stylist-workspace/reset-password?token=" + resetToken;
        String subject = "Liberty Salon - Reset Your Password";
        String body = "Hello " + employee.getEmployeeName() + ",\n\n" +
                      "We received a request to reset your password. Please click the link below to set a new password:\n\n" +
                      resetLink + "\n\n" +
                      "If you did not request this, please ignore this email.\n\n" +
                      "Best regards,\nLiberty Salon Team";

        try {
            emailSender.sendSimpleEmail(employee.getEmail(), subject, body);
        } catch (Exception mailEx) {
            System.err.println("Failed to send reset email: " + mailEx.getMessage());
            throw new AppException("Failed to send reset email. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(Map.of("message", "A password reset link has been sent to your email address."));
    }

    /**
     * Resets the password using a valid reset token.
     * Validates the token and email, then hashes and stores the new password.
     *
     * @param payload contains token, email, and password
     * @return success message
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String email = payload.get("email");
        String password = payload.get("password");

        if (token == null || email == null || password == null) {
            throw new AppException("Token, email, and password are required.", HttpStatus.BAD_REQUEST);
        }

        EmployeeRegEntity employee = employeeRegRepository
                .findByResetToken(token)
                .orElseThrow(() -> new AppException(
                        "Invalid or expired reset link. Please request a new password reset.",
                        HttpStatus.BAD_REQUEST));

        // Ensure the email matches the employee record
        if (!employee.getEmail().equalsIgnoreCase(email)) {
            throw new AppException(
                    "The email address does not match our records.",
                    HttpStatus.BAD_REQUEST);
        }

        // Hash and save the new password, clear the reset token
        employee.setPassword(passwordEncoder.encode(password));
        employee.setResetToken(null);
        employeeRegRepository.save(employee);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully. You can now log in with your new password."));
    }
}
