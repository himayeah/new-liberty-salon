package com.bit.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bit.backend.config.EmailSender;
import com.bit.backend.dtos.EmployeeRegDto;
import com.bit.backend.entities.EmployeeRegEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.EmployeeRegMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;
import com.bit.backend.repositories.EmployeeAttendanceRepository;
import com.bit.backend.repositories.EmployeeLeaveRepository;
import com.bit.backend.repositories.EmployeeRegRepository;
import com.bit.backend.repositories.EmployeeScheduleRepository;
import com.bit.backend.services.EmployeeRegServiceI;

@Service
public class EmployeeRegService implements EmployeeRegServiceI {

    private final EmployeeRegRepository employeeRegRepository;
    private final EmployeeRegMapper employeeRegMapper;
    private final EmailSender emailSender;
    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final EmployeeAttendanceRepository employeeAttendanceRepository;
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final EmployeeLeaveRepository employeeLeaveRepository;

    public EmployeeRegService(EmployeeRegRepository employeeRegRepository,
            EmployeeRegMapper employeeRegMapper,
            EmailSender emailSender,
            AppointmentScheduleRepository appointmentScheduleRepository,
            EmployeeAttendanceRepository employeeAttendanceRepository,
            EmployeeScheduleRepository employeeScheduleRepository,
            EmployeeLeaveRepository employeeLeaveRepository) {
        this.employeeRegRepository = employeeRegRepository;
        this.employeeRegMapper = employeeRegMapper;
        this.emailSender = emailSender;
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.employeeAttendanceRepository = employeeAttendanceRepository;
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.employeeLeaveRepository = employeeLeaveRepository;
    }

    @Override
    public EmployeeRegDto addEmployee(EmployeeRegDto employeeRegDto) {
        try {
            if (employeeRegDto.getEmail() != null && !employeeRegDto.getEmail().trim().isEmpty()) {
                List<EmployeeRegEntity> existing = employeeRegRepository.findAllByEmail(employeeRegDto.getEmail().trim());
                if (!existing.isEmpty()) {
                    throw new AppException("An employee with this email address is already registered.", HttpStatus.BAD_REQUEST);
                }
            }

            EmployeeRegEntity employeeRegEntity = employeeRegMapper.toEmployeeRegEntity(employeeRegDto);

            // Generate a secure invite token
            String token = UUID.randomUUID().toString();
            employeeRegEntity.setInviteToken(token);

            EmployeeRegEntity savedItem = employeeRegRepository.save(employeeRegEntity);
            EmployeeRegDto savedDto = employeeRegMapper.toEmployeeRegDto(savedItem);

            // Send email invite if email is present
            if (savedItem.getEmail() != null && !savedItem.getEmail().trim().isEmpty()) {
                String inviteLink = "http://localhost:4200/employee-workspace/invite?token=" + token;
                String subject = "Welcome to Liberty Salon - Set Your Password";
                String body = "Hello " + savedItem.getEmployeeName() + ",\n\n" +
                        "You have been registered as an employee at Liberty Salon. Please click the link below to set your password and access your workspace:\n\n"
                        +
                        inviteLink + "\n\n" +
                        "Best regards,\nLiberty Salon Team";
                try {
                    emailSender.sendSimpleEmail(savedItem.getEmail(), subject, body);
                } catch (Exception mailEx) {
                    System.err.println("Failed to send invite email: " + mailEx.getMessage());
                }
            }

            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<EmployeeRegDto> getData() {
        try {
            List<EmployeeRegEntity> employeeRegEntityList = employeeRegRepository.findAll();
            List<EmployeeRegDto> employeeRegDtoList = employeeRegMapper.toEmployeeRegDtoList(employeeRegEntityList);
            return employeeRegDtoList;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmployeeRegDto updateEmployeeReg(long id, EmployeeRegDto employeeRegDto) {
        try {
            Optional<EmployeeRegEntity> optionalEmployeeRegEntity = employeeRegRepository.findById(id);
            if (!optionalEmployeeRegEntity.isPresent()) {
                throw new AppException("Employee Reg Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            EmployeeRegEntity existing = optionalEmployeeRegEntity.get();

            if (employeeRegDto.getEmail() != null && !employeeRegDto.getEmail().trim().isEmpty()) {
                List<EmployeeRegEntity> duplicate = employeeRegRepository.findAllByEmail(employeeRegDto.getEmail().trim());
                for (EmployeeRegEntity emp : duplicate) {
                    if (!emp.getId().equals(id)) {
                        throw new AppException("An employee with this email address is already registered.", HttpStatus.BAD_REQUEST);
                    }
                }
            }

            EmployeeRegEntity newEmployeeRegEntity = employeeRegMapper.toEmployeeRegEntity(employeeRegDto);
            newEmployeeRegEntity.setId(id);

            // Preserve credentials and tokens from existing entity if they are not provided
            // in DTO
            if (newEmployeeRegEntity.getPassword() == null || newEmployeeRegEntity.getPassword().isEmpty()) {
                newEmployeeRegEntity.setPassword(existing.getPassword());
            }
            if (newEmployeeRegEntity.getInviteToken() == null || newEmployeeRegEntity.getInviteToken().isEmpty()) {
                newEmployeeRegEntity.setInviteToken(existing.getInviteToken());
            }
            if (newEmployeeRegEntity.getResetToken() == null || newEmployeeRegEntity.getResetToken().isEmpty()) {
                newEmployeeRegEntity.setResetToken(existing.getResetToken());
            }

            EmployeeRegEntity employeeRegEntity = employeeRegRepository.save(newEmployeeRegEntity);
            EmployeeRegDto employeeRegDtoRes = employeeRegMapper.toEmployeeRegDto(employeeRegEntity);
            return employeeRegDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public EmployeeRegDto deleteEmployeeReg(long id) {
        try {
            Optional<EmployeeRegEntity> optionalEmployeeRegEntity = employeeRegRepository.findById(id);
            if (!optionalEmployeeRegEntity.isPresent()) {
                throw new AppException("Employee Reg Does Not Exist", HttpStatus.BAD_REQUEST);
            }

            EmployeeRegEntity employeeToDelete = optionalEmployeeRegEntity.get();

            appointmentScheduleRepository.findAll().stream()
                    .filter(appointment -> appointment.getEmployee() != null
                            && appointment.getEmployee().getId() != null
                            && appointment.getEmployee().getId().equals(id))
                    .forEach(appointmentScheduleRepository::delete);

            employeeAttendanceRepository.findAll().stream()
                    .filter(attendance -> attendance.getEmployeeRegEntity() != null
                            && attendance.getEmployeeRegEntity().getId() != null
                            && attendance.getEmployeeRegEntity().getId().equals(id))
                    .forEach(employeeAttendanceRepository::delete);

            employeeScheduleRepository.findAll().stream()
                    .filter(schedule -> schedule.getEmployeeRegEntity() != null
                            && schedule.getEmployeeRegEntity().getId() != null
                            && schedule.getEmployeeRegEntity().getId().equals(id))
                    .forEach(employeeScheduleRepository::delete);

            employeeLeaveRepository.findAll().stream()
                    .filter(leave -> leave.getEmployeeRegEntity() != null
                            && leave.getEmployeeRegEntity().getId() != null
                            && leave.getEmployeeRegEntity().getId().equals(id))
                    .forEach(employeeLeaveRepository::delete);

            employeeRegRepository.deleteById(id);
            return employeeRegMapper.toEmployeeRegDto(employeeToDelete);
        } catch (Exception e) {
            throw new AppException("Request filled with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public EmployeeRegDto getById(long id) {
        try {
            Optional<EmployeeRegEntity> optionalEmployeeRegEntity = employeeRegRepository.findById(id);
            if (!optionalEmployeeRegEntity.isPresent()) {
                throw new AppException("Employee Reg Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            return employeeRegMapper.toEmployeeRegDto(optionalEmployeeRegEntity.get());
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
