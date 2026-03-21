package com.bit.backend.services.impl;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.dtos.UserDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import com.bit.backend.entities.ClientRegEntity;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.entities.ServiceEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.AppointmentScheduleMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;
import com.bit.backend.repositories.ClientRegRepository;
import com.bit.backend.repositories.EmployeeRegRepository;
import com.bit.backend.repositories.ServiceRepository;
import com.bit.backend.services.AppointmentScheduleServiceI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentScheduleService implements AppointmentScheduleServiceI {

    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final AppointmentScheduleMapper appointmentScheduleMapper;
    private final ClientRegRepository clientRegRepository;
    private final EmployeeRegRepository employeeRegRepository;
    private final ServiceRepository serviceRepository;

    public AppointmentScheduleService(AppointmentScheduleRepository appointmentScheduleRepository,
            AppointmentScheduleMapper appointmentScheduleMapper,
            ClientRegRepository clientRegRepository,
            EmployeeRegRepository employeeRegRepository,
            ServiceRepository serviceRepository) {
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.appointmentScheduleMapper = appointmentScheduleMapper;
        this.clientRegRepository = clientRegRepository;
        this.employeeRegRepository = employeeRegRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public AppointmentScheduleDto addAppointment(AppointmentScheduleDto appointmentScheduleDto) {
        try {
            AppointmentScheduleEntity entity = appointmentScheduleMapper
                    .toAppointmentScheduleEntity(appointmentScheduleDto);
            setRelations(entity, appointmentScheduleDto);

            // Set audit fields
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            entity.setCreatedDate(now);
            entity.setCreatedBy(getCurrentUser());

            if ("CANCELLED".equals(entity.getAppointmentStatus())) {
                entity.setCancelledDate(now);
            }

            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(entity);
            return appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);
        } catch (Exception e) {
            throw new AppException("Failed to add appointment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<AppointmentScheduleDto> getAppointments() {
        try {
            List<AppointmentScheduleEntity> entities = appointmentScheduleRepository.findAll();
            return appointmentScheduleMapper.toAppointmentScheduleDtoList(entities);
        } catch (Exception e) {
            throw new AppException("Failed to fetch appointments: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppointmentScheduleDto updateAppointment(long id, AppointmentScheduleDto appointmentScheduleDto) {
        try {
            AppointmentScheduleEntity existingEntity = appointmentScheduleRepository.findById(id)
                    .orElseThrow(() -> new AppException("Appointment Schedule Does Not Exist", HttpStatus.NOT_FOUND));

            AppointmentScheduleEntity entity = appointmentScheduleMapper
                    .toAppointmentScheduleEntity(appointmentScheduleDto);
            entity.setId(id);
            setRelations(entity, appointmentScheduleDto);

            // Preserve audit fields
            entity.setCreatedDate(existingEntity.getCreatedDate());
            entity.setCreatedBy(existingEntity.getCreatedBy());

            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Update cancelled date if status changed to CANCELLED
            if ("CANCELLED".equals(entity.getAppointmentStatus())
                    && !"CANCELLED".equals(existingEntity.getAppointmentStatus())) {
                entity.setCancelledDate(now);
            } else {
                entity.setCancelledDate(existingEntity.getCancelledDate());
            }

            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(entity);
            return appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update appointment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public AppointmentScheduleDto deleteAppointment(long id) {
        try {
            Optional<AppointmentScheduleEntity> entity = appointmentScheduleRepository.findById(id);
            if (entity.isEmpty()) {
                throw new AppException("Appointment Schedule Does Not Exist", HttpStatus.NOT_FOUND);
            }
            appointmentScheduleRepository.deleteById(id);
            return appointmentScheduleMapper.toAppointmentScheduleDto(entity.get());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete appointment: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<AppointmentScheduleDto> getAppointment() {
        return getAppointments();
    }

    private void setRelations(AppointmentScheduleEntity entity, AppointmentScheduleDto dto) {
        if (dto.getClientId() != null) {
            ClientRegEntity client = clientRegRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new AppException("Client not found", HttpStatus.BAD_REQUEST));
            entity.setClient(client);
        }
        if (dto.getEmployeeId() != null) {
            EmployeeRegEntity employee = employeeRegRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new AppException("Employee not found", HttpStatus.BAD_REQUEST));
            entity.setEmployee(employee);
        }
        if (dto.getServiceId() != null) {
            ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new AppException("Service not found", HttpStatus.BAD_REQUEST));
            entity.setService(service);
        }
    }

    // getting current user for "created_by"
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDto) {
            return ((UserDto) authentication.getPrincipal()).getLogin();
        }
        return "system";
    }

    @Override
    public long countAppointmentsLast30Days() {
        String sinceDate = LocalDateTime.now().minusDays(30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return appointmentScheduleRepository.countAppointmentsAfter(sinceDate);
    }
}
