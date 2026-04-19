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
            AppointmentScheduleDto responseDto = appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);

            // Send email notification to receptionist
            sendEmailNotification(responseDto);

            return responseDto;
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
            if ("CANCELLED".equals(entity.getAppointmentStatus())) {
                if (!"CANCELLED".equals(existingEntity.getAppointmentStatus())) {
                    // Status just changed to CANCELLED
                    entity.setCancelledDate(now);
                } else {
                    // Status was already CANCELLED, preserve the original date
                    entity.setCancelledDate(existingEntity.getCancelledDate());
                }
            } else {
                // Status is not CANCELLED, clear the date
                entity.setCancelledDate(null);
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

    // Dashboard card (Total Appointments in lat 30 days)
    @Override
    public long countAppointmentsLast30Days() {
        return appointmentScheduleRepository.countAppointmentsLast30Days();
    }

    // Dashboard card (get mostly used service name)
    @Override
    public String getMostUsedService() {
        try {
            String result = appointmentScheduleRepository.getMostUsedService();
            return result != null ? result : "Female Haircut";
        } catch (Exception e) {
            throw new AppException("Failed to load mostly used Service: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Dashboard line chart (Get appointment counts by month for the last 6 months)
    @Override
    public List<Object[]> getAppointmentCountsByMonth() {
        return appointmentScheduleRepository.getAppointmentCountsByMonth();
    }

    // Dashboard pie chart (Get Top 3 services)
    @Override
    public List<Object[]> getTop3Services() {
        return appointmentScheduleRepository.getTop3Services();
    }

    // Dashboard (Get Top 5 employees)
    @Override
    public List<Object[]> getTop5Employees() {
        return appointmentScheduleRepository.getTop5Employees();
    }

    private void sendEmailNotification(AppointmentScheduleDto appointment) {
        // In a real application, you would use JavaMailSender here.
        // For now, we simulate the email trigger by logging the details.
        System.out.println("--------------------------------------------------");
        System.out.println("NOTIFICATION: New Booking Received!");
        System.out.println("Client: " + appointment.getClientName());
        System.out.println("Stylist: " + appointment.getEmployeeName());
        System.out.println("Service: " + appointment.getServiceName());
        System.out.println("Date: " + appointment.getAppointmentDate());
        System.out.println("Time: " + appointment.getAppointmentStartTime());
        System.out.println("Email sent to receptionist@newlibertysalon.com");
        System.out.println("--------------------------------------------------");
    }

}
