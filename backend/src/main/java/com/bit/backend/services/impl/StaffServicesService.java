package com.bit.backend.services.impl;

import com.bit.backend.dtos.StaffServicesDto;
import com.bit.backend.entities.StaffServicesEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.StaffServicesMapper;
import com.bit.backend.repositories.StaffServicesRepository;
import com.bit.backend.services.StaffServicesServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServicesService implements StaffServicesServiceI {

    private final StaffServicesRepository staffServicesRepository;
    private final StaffServicesMapper staffServicesMapper;

    public StaffServicesService(StaffServicesRepository staffServicesRepository, StaffServicesMapper staffServicesMapper) {
        this.staffServicesRepository = staffServicesRepository;
        this.staffServicesMapper = staffServicesMapper;
    }
    @Override
    public StaffServicesDto createStaffServices(StaffServicesDto staffServicesDto){
        try{
            System.out.println("Name  : " + staffServicesDto.getServiceName());
            System.out.println("Id : " + staffServicesDto.getId());
            StaffServicesEntity staffServicesEntity = staffServicesMapper.toStaffServicesEntity(staffServicesDto);
            StaffServicesEntity savedItem = staffServicesRepository.save(staffServicesEntity);
            StaffServicesDto savedDto = staffServicesMapper.toStaffServicesDto(savedItem);
            return savedDto;
        } catch (Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<StaffServicesDto> getStaffServices() {
        try{
            List<StaffServicesEntity> staffServicesEntityList = staffServicesRepository.findAll();
            List<StaffServicesDto> staffServicesDtoList = staffServicesMapper.toStaffServicesDtoList(staffServicesEntityList);
            return staffServicesDtoList;
        } catch(Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public StaffServicesDto updateStaffServices(long id, StaffServicesDto staffServicesDto) {
       try{
           Optional<StaffServicesEntity> optionalStaffServicesEntity = staffServicesRepository.findById(id);
           if(!optionalStaffServicesEntity.isPresent()){
               throw new AppException("Staff Service does not exist", HttpStatus.NOT_FOUND);
           }
           StaffServicesEntity newStaffServicesEntity = staffServicesMapper.toStaffServicesEntity(staffServicesDto);
           newStaffServicesEntity.setId(id);
           StaffServicesEntity staffServicesEntity = staffServicesRepository.save(newStaffServicesEntity);
           StaffServicesDto responseStaffServicesDto = staffServicesMapper.toStaffServicesDto(staffServicesEntity);
           return responseStaffServicesDto;
       }
        catch(Exception e){
           throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public StaffServicesDto deleteStaffServices(long id) {
        try{
            Optional<StaffServicesEntity> optionalStaffServicesEntity = staffServicesRepository.findById(id);
            if(!optionalStaffServicesEntity.isPresent()){
                throw new AppException("Staff Service does not exist", HttpStatus.NOT_FOUND);
            }
            staffServicesRepository.deleteById(id);
            return staffServicesMapper.toStaffServicesDto(optionalStaffServicesEntity.get());
        }
        catch (Exception e){
            throw new AppException("Request failed with error:" + e, HttpStatus.BAD_REQUEST);
        }
    }


}
