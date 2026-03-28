package com.bit.backend.mappers;

import com.bit.backend.dtos.ClientRegDto;
import com.bit.backend.entities.ClientRegEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientRegMapper {

    @Mapping(target = "registrationYear", ignore = true)
    @Mapping(target = "totalRegistrations", ignore = true)
    ClientRegDto toClientRegDto(ClientRegEntity clientRegEntity);

    @Mapping(target = "registrationDate", ignore = true)
    ClientRegEntity toClientRegEntity(ClientRegDto clientRegDto);
    
    List<ClientRegDto> toClientRegDtoList(List<ClientRegEntity> clientRegEntityList);

}
