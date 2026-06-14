package com.bit.backend.controllers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bit.backend.config.EmailSender;
import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.dtos.StylistLoginDto;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.mappers.EmployeeRegMapper;
import com.bit.backend.repositories.EmployeeRegRepository;

@ExtendWith(MockitoExtension.class)
class StylistAuthControllerTest {

    @Mock
    private EmployeeRegRepository employeeRegRepository;

    @Mock
    private EmployeeRegMapper employeeRegMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private StylistAuthController stylistAuthController;

    @Test
    void stylistLoginShouldAcceptLegacyPlainTextPassword() {
        EmployeeRegEntity employee = new EmployeeRegEntity();
        employee.setId(1L);
        employee.setEmail("stylist@example.com");
        employee.setPassword("Password123");

        StylistLoginDto loginDto = new StylistLoginDto("stylist@example.com", "Password123");

        when(employeeRegRepository.findByEmail("stylist@example.com")).thenReturn(Optional.of(employee));
        when(employeeRegMapper.toEmployeeRegDto(employee)).thenReturn(new EmployeeRegDto());

        ResponseEntity<EmployeeRegDto> response = stylistAuthController.stylistLogin(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
