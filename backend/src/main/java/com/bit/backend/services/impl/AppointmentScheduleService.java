package com.bit.backend.services.impl;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.dtos.ClientLifeTimeValueDto;
import com.bit.backend.dtos.ReportProductSalesDto;
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
import com.bit.backend.services.NotificationService;

@Service
public class AppointmentScheduleService implements AppointmentScheduleServiceI {

    private static final LocalTime SALON_OPENING_TIME = LocalTime.of(10, 0);
    private static final LocalTime SALON_CLOSING_TIME = LocalTime.of(18, 0);

    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final AppointmentScheduleMapper appointmentScheduleMapper;
    private final ClientRegRepository clientRegRepository;
    private final EmployeeRegRepository employeeRegRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationService notificationService;

    public AppointmentScheduleService(AppointmentScheduleRepository appointmentScheduleRepository,
            AppointmentScheduleMapper appointmentScheduleMapper,
            ClientRegRepository clientRegRepository,
            EmployeeRegRepository employeeRegRepository,
            ServiceRepository serviceRepository,
            NotificationService notificationService) {
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.appointmentScheduleMapper = appointmentScheduleMapper;
        this.clientRegRepository = clientRegRepository;
        this.employeeRegRepository = employeeRegRepository;
        this.serviceRepository = serviceRepository;
        this.notificationService = notificationService;
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

            normalizeAndValidateAppointmentTimes(entity);
            formatAppointmentDate(entity);

            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(entity);
            AppointmentScheduleDto responseDto = appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);

            // Ensure client phone number is populated for notifications
            if (savedItem.getClient() != null
                    && (responseDto.getClientPhone() == null || responseDto.getClientPhone().isEmpty())) {
                responseDto.setClientPhone(savedItem.getClient().getPhoneNumber());
            }

            // Calls the sendAppointmentNotification() in NotificationService to send the
            // Notification after client is added
            notificationService.sendAppointmentNotification(responseDto);

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

    // Retrieve the Max ID, Increment that value from 10 and return the fina Result
    // - Q2
    @Override
    public Long getMaxId() {
        try {
            Long maxId = appointmentScheduleRepository.getMaxId();
            return (maxId * 10);
        } catch (Exception e) {
            throw new AppException("Failed to fetch max ID: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppointmentScheduleDto updateAppointment(long id, AppointmentScheduleDto appointmentScheduleDto) {
        try {
            // retireves the entire client entity of the given client ID
            ClientRegEntity clientRegEntity = clientRegRepository
                    .findByIdByGivenClientId(appointmentScheduleDto.getClientId());
            clientRegEntity.setLastVisitedDate(appointmentScheduleDto.getAppointmentDate());
            clientRegRepository.save(clientRegEntity);

            // gets the existing record for the given id
            AppointmentScheduleEntity existingEntity = appointmentScheduleRepository.findById(id)
                    .orElseThrow(() -> new AppException("Appointment Schedule Does Not Exist", HttpStatus.NOT_FOUND));

            AppointmentScheduleEntity appointmentScheduleEntity = appointmentScheduleMapper
                    .toAppointmentScheduleEntity(appointmentScheduleDto);
            // sets the id retireved from URL for best practice
            appointmentScheduleEntity.setId(id);

            // When you update an appointment, the frontend sends only the fields the user
            // changed. frontend doesn't update createdDate, createdBy etc
            // so you get the existing entity and update those fields here
            setRelations(appointmentScheduleEntity, appointmentScheduleDto);
            appointmentScheduleEntity.setCreatedDate(existingEntity.getCreatedDate());
            appointmentScheduleEntity.setCreatedBy(existingEntity.getCreatedBy());
            appointmentScheduleEntity.setReminderSent(existingEntity.getReminderSent());

            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if ("CANCELLED".equals(appointmentScheduleEntity.getAppointmentStatus())) {
                if (!"CANCELLED".equals(existingEntity.getAppointmentStatus())) {
                    appointmentScheduleEntity.setCancelledDate(now);
                } else {
                    appointmentScheduleEntity.setCancelledDate(existingEntity.getCancelledDate());
                }
            } else {
                appointmentScheduleEntity.setCancelledDate(null);
            }

            normalizeAndValidateAppointmentTimes(appointmentScheduleEntity);
            formatAppointmentDate(appointmentScheduleEntity);

            boolean isNewCheckIn = "CHECK_IN".equalsIgnoreCase(appointmentScheduleEntity.getAppointmentStatus())
                    || "CHECKED_IN".equalsIgnoreCase(appointmentScheduleEntity.getAppointmentStatus());
            boolean wasCheckIn = "CHECK_IN".equalsIgnoreCase(existingEntity.getAppointmentStatus())
                    || "CHECKED_IN".equalsIgnoreCase(existingEntity.getAppointmentStatus());

            if (isNewCheckIn && !wasCheckIn) {
                AppointmentScheduleDto notificationDto = appointmentScheduleMapper
                        .toAppointmentScheduleDto(appointmentScheduleEntity);
                notificationService.sendStylistCheckInNotification(notificationDto);
            }

            if ("COMPLETED".equals(appointmentScheduleDto.getAppointmentStatus())) {
                // All client details related to given client id of the updating appointment
                ClientRegEntity newClientRegEntity = clientRegRepository
                        .findByIdByGivenClientId(appointmentScheduleDto.getClientId());
                // A list of clientId and lifeTimeValues
                List<ClientLifeTimeValueDto> clientLifeTimeValueDto = clientRegRepository.getClientLifetimeValue();
                for (ClientLifeTimeValueDto newClientLifeTimeValueDto : clientLifeTimeValueDto) {
                    if (newClientLifeTimeValueDto.getClientId() == appointmentScheduleDto.getClientId()) {
                        newClientRegEntity.setLifetimeValue(newClientLifeTimeValueDto.getLifetimeValue());
                        clientRegRepository.save(newClientRegEntity);
                    }
                }
            }

            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(appointmentScheduleEntity);
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
    public AppointmentScheduleDto getAppointmentById(long id) {
        try {
            AppointmentScheduleEntity entity = appointmentScheduleRepository.findById(id)
                    .orElseThrow(() -> new AppException("Appointment Schedule Does Not Exist", HttpStatus.NOT_FOUND));
            return appointmentScheduleMapper.toAppointmentScheduleDto(entity);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to fetch appointment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        List<Object[]> result = appointmentScheduleRepository.getTop3Services();
        // System out should come before return
        System.out.println("Dashboard Pie Chart Top 3 Appointments:" + result);
        return result;
    }

    // Dashboard (Get Top 5 employees)
    @Override
    public List<Object[]> getTop5Employees() {
        return appointmentScheduleRepository.getTop5Employees();
    }

    // Dashboard Appointment Timeline Notification (Next 30 minutes)
    @Override
    public List<AppointmentScheduleDto> getUpcomingNotifications() {
        List<AppointmentScheduleEntity> entities = appointmentScheduleRepository.findUpcomingNotifications();

        // Mark as notified before returning
        for (AppointmentScheduleEntity entity : entities) {
            entity.setDashboardNotified(true);
        }
        appointmentScheduleRepository.saveAll(entities);

        return appointmentScheduleMapper.toAppointmentScheduleDtoList(entities);
    }

    private void normalizeAndValidateAppointmentTimes(AppointmentScheduleEntity entity) {
        if ((entity.getAppointmentEndTime() == null || entity.getAppointmentEndTime().isBlank())
                && entity.getAppointmentStartTime() != null) {
            LocalTime start = parseTime(entity.getAppointmentStartTime());
            if (start != null) {
                int durationMinutes = 60;
                if (entity.getService() != null && entity.getService().getDuration() != null
                        && entity.getService().getDuration() > 0) {
                    durationMinutes = entity.getService().getDuration();
                }
                LocalTime end = start.plusMinutes(durationMinutes);
                entity.setAppointmentEndTime(end.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        }

        String startTime = normalizeTime(entity.getAppointmentStartTime());
        String endTime = normalizeTime(entity.getAppointmentEndTime());

        entity.setAppointmentStartTime(startTime);
        entity.setAppointmentEndTime(endTime);

        LocalTime start = parseTime(startTime);
        LocalTime end = parseTime(endTime);

        if (start == null || end == null) {
            throw new AppException("Appointment times must be in HH:mm or h:mm AM/PM format", HttpStatus.BAD_REQUEST);
        }

        if (start.isBefore(SALON_OPENING_TIME) || end.isAfter(SALON_CLOSING_TIME) || !end.isAfter(start)) {
            throw new AppException(
                    "Appointments must be booked between 10:00 AM and 06:00 PM, with end time after start time",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private String normalizeTime(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        LocalTime parsed = parseTime(value);
        return parsed != null ? parsed.format(DateTimeFormatter.ofPattern("HH:mm")) : value;
    }

    private LocalTime parseTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String trimmed = value.trim();
        for (DateTimeFormatter formatter : List.of(
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("H:mm"),
                DateTimeFormatter.ofPattern("hh:mm a", Locale.US),
                DateTimeFormatter.ofPattern("h:mm a", Locale.US))) {
            try {
                return LocalTime.parse(trimmed, formatter);
            } catch (Exception ignored) {
                // Try the next formatter
            }
        }

        return null;
    }

    private void formatAppointmentDate(AppointmentScheduleEntity entity) {
        if (entity.getAppointmentDate() != null && entity.getAppointmentDate().contains("T")) {
            entity.setAppointmentDate(entity.getAppointmentDate().split("T")[0]);
        }
    }

    // Status update (Client check in)
    // Compares the old status against the new status
    // Only if the status has changed, the notification sends to stylist

    // @Override
    // public AppointmentScheduleDto updateStatus(long id, AppointmentScheduleDto
    // appointmentScheduleDto) {
    // try {
    // Optional<AppointmentScheduleEntity> optionalEntity =
    // appointmentScheduleRepository.findById(id);
    // if (!optionalEntity.isPresent()) {
    // throw new AppException("Appointment Schedule Does Not Exist",
    // HttpStatus.NOT_FOUND);
    // }
    // AppointmentScheduleEntity entity = optionalEntity.get();

    // String oldStatus = entity.getAppointmentStatus();
    // String newStatus = appointmentScheduleDto.getAppointmentStatus();
    // entity.setAppointmentStatus(newStatus);

    // AppointmentScheduleEntity savedItem =
    // appointmentScheduleRepository.save(entity);
    // AppointmentScheduleDto responseDto =
    // appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);

    // if (isCheckInStatus(newStatus) && !isCheckInStatus(oldStatus)) {
    // notificationService.sendStylistCheckInNotification(responseDto);
    // }

    // return responseDto;
    // } catch (AppException e) {
    // throw e;
    // } catch (Exception e) {
    // throw new AppException("Request failed with error:" + e,
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    // private boolean isCheckInStatus(String status) {
    // return "CHECK_IN".equalsIgnoreCase(status) ||
    // "CHECKED_IN".equalsIgnoreCase(status);
    // }

    // We need to check whether the status sent from the frontend is CHECK_IN, if
    // yes trigger the notification to Stylist
    @Override
    public AppointmentScheduleDto updateStatus(long id, AppointmentScheduleDto appointmentScheduleDto) {
        try {
            // checks whether the data exists in the backend
            // If yes, DB will return an Entity like;
            // Appointment #5
            // status: BOOKED
            // client: John
            // date: ...
            // If an Appointment with the requested ID doesn't exist, it will give
            // "Appointment Not Found" with a 404 error (not found)
            Optional<AppointmentScheduleEntity> optionalEntity = appointmentScheduleRepository.findById(id);
            if (!optionalEntity.isPresent()) {
                throw new AppException("Appointment Not Found", HttpStatus.NOT_FOUND);
            }

            // Open the box and take the appointment ou, now you have and entity;
            // status = BOOKED
            // client = John
            // date = ...
            // get the status and put it in a variable called oldStatus
            AppointmentScheduleEntity entity = optionalEntity.get();

            // get the old status from the entity returned from DB and put it in a variable
            // called oldStatus
            String oldStatus = entity.getAppointmentStatus();

            // get the new status received from the frontend through appointmentScheduleDto
            // and put it in a variable called newStatus
            String newStatus = appointmentScheduleDto.getAppointmentStatus();

            // set the new status into the entity returned from DB
            // status = CHECKED_IN
            // client = John
            // ...
            entity.setAppointmentStatus(newStatus);

            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(entity);
            AppointmentScheduleDto responseDto = appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);

            // Is the NEW status CHECK_IN?
            // Is the OLD status NOT CHECK_IN?
            // If both conditions are true, then send the notification to stylist
            if (isCheckInStatus(newStatus) && !isCheckInStatus(oldStatus)) {
                notificationService.sendStylistCheckInNotification(responseDto);
            }

            // Send the response back to the frontend
            return responseDto;

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Request failed with error:" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isCheckInStatus(String status) {
        return "CHECK_IN".equalsIgnoreCase(status) || "CHECKED_IN".equalsIgnoreCase(status);
    }

    // Dashboard card (Top employee Name) NEWLYADDED
    @Override
    public List<AppointmentScheduleDto> getTopEmployeeName() {
        try {
            List<Object[]> rows = appointmentScheduleRepository.getTopEmployeeName();
            System.out.println("Top Employee Name Newly Added:" + rows);
            List<AppointmentScheduleDto> list = new ArrayList<>();
            for (Object[] row : rows) {
                AppointmentScheduleDto appointmentScheduleDto = new AppointmentScheduleDto();
                appointmentScheduleDto.setTopEmployeeName((String) row[0]);
                appointmentScheduleDto.setTopTotalAppointments(((Number) row[1]).longValue());
                list.add(appointmentScheduleDto);
                System.out.println("Produuct Sales DTO after Data:" + appointmentScheduleDto);
            }

            return list;

        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
