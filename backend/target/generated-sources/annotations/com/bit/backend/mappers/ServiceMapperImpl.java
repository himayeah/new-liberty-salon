package com.bit.backend.mappers;

import com.bit.backend.dtos.ServiceDto;
import com.bit.backend.entities.ServiceEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-03T20:00:35+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceDto toServiceDto(ServiceEntity serviceEntity) {
        if ( serviceEntity == null ) {
            return null;
        }

        ServiceDto serviceDto = new ServiceDto();

        serviceDto.setColorCode( serviceEntity.getColorCode() );
        serviceDto.setCommission( serviceEntity.getCommission() );
        serviceDto.setDescription( serviceEntity.getDescription() );
        if ( serviceEntity.getId() != null ) {
            serviceDto.setId( serviceEntity.getId() );
        }
        if ( serviceEntity.getIsActive() != null ) {
            serviceDto.setIsActive( Boolean.parseBoolean( serviceEntity.getIsActive() ) );
        }
        serviceDto.setPrice( serviceEntity.getPrice() );
        serviceDto.setServiceName( serviceEntity.getServiceName() );

        return serviceDto;
    }

    @Override
    public ServiceEntity toServiceEntity(ServiceDto serviceDto) {
        if ( serviceDto == null ) {
            return null;
        }

        String colorCode = null;
        BigDecimal commission = null;
        String description = null;
        Long id = null;
        String isActive = null;
        BigDecimal price = null;
        String serviceName = null;

        colorCode = serviceDto.getColorCode();
        commission = serviceDto.getCommission();
        description = serviceDto.getDescription();
        id = serviceDto.getId();
        if ( serviceDto.getIsActive() != null ) {
            isActive = String.valueOf( serviceDto.getIsActive() );
        }
        price = serviceDto.getPrice();
        serviceName = serviceDto.getServiceName();

        ServiceEntity serviceEntity = new ServiceEntity( id, serviceName, price, commission, colorCode, description, isActive );

        return serviceEntity;
    }

    @Override
    public List<ServiceDto> toServiceDtoList(List<ServiceEntity> serviceEntityList) {
        if ( serviceEntityList == null ) {
            return null;
        }

        List<ServiceDto> list = new ArrayList<ServiceDto>( serviceEntityList.size() );
        for ( ServiceEntity serviceEntity : serviceEntityList ) {
            list.add( toServiceDto( serviceEntity ) );
        }

        return list;
    }
}
