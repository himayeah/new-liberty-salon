package com.bit.backend.services.impl;

import com.bit.backend.dtos.FormDemoDto;
import com.bit.backend.entities.FormDemoEntity;
import com.bit.backend.mappers.FormDemoMapper;
import com.bit.backend.repositories.FormDemoRepository;
import com.bit.backend.services.FormDemoServiceI;
import org.springframework.stereotype.Service;



@Service
public class FormDemoService implements FormDemoServiceI {

    private final FormDemoRepository formDemoRepository;
    private final FormDemoMapper formDemoMapper;


    public FormDemoService(FormDemoRepository formDemoRepository, FormDemoMapper formDemoMapper) {
        this.formDemoRepository = formDemoRepository;
        this.formDemoMapper = formDemoMapper;
    }

    @Override
    public FormDemoDto addFormDemo(FormDemoDto formDemoDto) {
        System.out.println("***** In BackEnd") ;

        FormDemoEntity formDemoEntity = formDemoMapper.toFormDemoEntity(formDemoDto);
        FormDemoEntity savedItems = formDemoRepository.save(formDemoEntity);

        FormDemoDto savedDto = formDemoMapper.toFormDemoDto(savedItems);
        return savedDto;
    }
}
