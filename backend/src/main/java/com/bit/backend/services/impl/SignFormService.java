package com.bit.backend.services.impl;

import com.bit.backend.dtos.SignFormDto;
import com.bit.backend.entities.SignFormEntity;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.mappers.SignFormMapper;
import com.bit.backend.repositories.FormDemoRepository;
import com.bit.backend.repositories.SignFormRepository;
import com.bit.backend.services.SignFormServiceI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignFormService implements SignFormServiceI {

    private final SignFormRepository signFormRepository;
    private final SignFormMapper signFormMapper;
    private final FormDemoRepository formDemoRepository;

    public SignFormService(SignFormRepository signFormRepository, SignFormMapper signFormMapper, FormDemoRepository formDemoRepository) {
        this.signFormRepository = signFormRepository;
        this.signFormMapper = signFormMapper;
        this.formDemoRepository = formDemoRepository;
    }

    @Override
    public SignFormDto addSignForm(SignFormDto signFormDto) {
        try{
            System.out.println("****You're In the Backend***");

            SignFormEntity signFormEntity = signFormMapper.toSignFormEntity(signFormDto);
            SignFormEntity savedSignFormEntity = signFormRepository.save(signFormEntity);
            return signFormMapper.toSignFormDto(savedSignFormEntity);
        } catch (Exception e) {
            throw new AppException("Request failed wiyh error:"+ e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public List<SignFormDto> getData() {
        try {
            System.out.println("****Fetching Data from Backend***");
            List<SignFormEntity> signFormEntityList = signFormRepository.findAll();
            return signFormMapper.toSignFormDtoList(signFormEntityList);
        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public SignFormDto updateSignForm(long id, SignFormDto signFormDto) {
        try {
            System.out.println("****Updating Sign Form in Backend***");

            Optional<SignFormEntity> optionalSignFormEntity = signFormRepository.findById(id);
            if (!optionalSignFormEntity.isPresent()) {
                throw new AppException("Form Demo does not exist", HttpStatus.BAD_REQUEST);
            }

            SignFormEntity newSignFormEntity = signFormMapper.toSignFormEntity(signFormDto);
            newSignFormEntity.setId(id); // Ensure the ID remains the same

            SignFormEntity updatedSignFormEntity = signFormRepository.save(newSignFormEntity);
            return signFormMapper.toSignFormDto(updatedSignFormEntity);

        } catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public SignFormDto deleteSignForm(long id) {
        try {

            Optional<SignFormEntity> optionalSignFormEntity = signFormRepository.findById(id);

            if (!optionalSignFormEntity.isPresent()) {
                throw new AppException("Form Demo does not exist", HttpStatus.BAD_REQUEST);
            }

            signFormRepository.deleteById(id); // Corrected repository for deletion
            return signFormMapper.toSignFormDto(optionalSignFormEntity.get());
        }

        catch (Exception e) {
            throw new AppException("Request failed with error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
