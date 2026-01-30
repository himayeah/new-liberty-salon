package com.bit.backend.services.impl;

import com.bit.backend.dtos.AppointmentScheduleDto;
import com.bit.backend.entities.AppointmentScheduleEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.AppointmentScheduleMapper;
import com.bit.backend.repositories.AppointmentScheduleRepository;
import com.bit.backend.services.AppointmentScheduleServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentScheduleService  implements AppointmentScheduleServiceI {

    private final AppointmentScheduleRepository appointmentScheduleRepository;
    private final AppointmentScheduleMapper appointmentScheduleMapper;

    public AppointmentScheduleService(AppointmentScheduleRepository appointmentScheduleRepository, AppointmentScheduleMapper appointmentScheduleMapper) {
        this.appointmentScheduleRepository = appointmentScheduleRepository;
        this.appointmentScheduleMapper = appointmentScheduleMapper;

    }

    @Override
    public AppointmentScheduleDto addAppointment(AppointmentScheduleDto appointmentScheduleDto) {
        try{
            AppointmentScheduleEntity appointmentScheduleEntity = appointmentScheduleMapper.toAppointmentScheduleEntity(appointmentScheduleDto);
            AppointmentScheduleEntity savedItem = appointmentScheduleRepository.save(appointmentScheduleEntity);
            AppointmentScheduleDto savedDto = appointmentScheduleMapper.toAppointmentScheduleDto(savedItem);
            return savedDto;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<AppointmentScheduleDto> getAppointment() {
        return List.of();
    }

    @Override
    public AppointmentScheduleDto updateAppointment(long id, AppointmentScheduleDto appointmentScheduleDto) {
        try {
            Optional<AppointmentScheduleEntity> optionalAppointmentScheduleEntity = appointmentScheduleRepository.findById(id);
            if (!optionalAppointmentScheduleEntity.isPresent()) {
                throw new AppException("Appointment Schedule Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            AppointmentScheduleEntity newAppointmentScheduleEntity = appointmentScheduleMapper.toAppointmentScheduleEntity(appointmentScheduleDto);
            newAppointmentScheduleEntity.setId(id);
            AppointmentScheduleEntity appointmentScheduleEntity = appointmentScheduleRepository.save(newAppointmentScheduleEntity);
            AppointmentScheduleDto appointmentScheduleDtoRes = appointmentScheduleMapper.toAppointmentScheduleDto(appointmentScheduleEntity);
            System.out.println("Updated Successfully:" + appointmentScheduleDtoRes.getFirstName());
            return appointmentScheduleDtoRes;
        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public AppointmentScheduleDto deleteAppointment(long id) {
        try{
            Optional<AppointmentScheduleEntity> optionalAppointmentScheduleEntity = appointmentScheduleRepository.findById(id);
            if (!optionalAppointmentScheduleEntity.isPresent()) {
                throw new AppException("Appointment Schedule Does Not Exist", HttpStatus.BAD_REQUEST);
            }
            appointmentScheduleRepository.deleteById(id);
            return appointmentScheduleMapper.toAppointmentScheduleDto(optionalAppointmentScheduleEntity.get());
        } catch (Exception e) {
            throw  new AppException("Request failed with error" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<AppointmentScheduleDto> getAppointments() {
        return List.of();
    }

}
