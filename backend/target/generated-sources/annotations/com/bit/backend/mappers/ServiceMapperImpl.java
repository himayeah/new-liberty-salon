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
    date = "2026-02-03T22:45:01+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public ServiceDto toServiceDto(ServiceEntity serviceEntity) {
        if ( serviceEntity == null ) {
            return null;
        }

        ServiceDto serviceDto = new ServiceDto();

        if ( serviceEntity.getId() != null ) {
            serviceDto.setId( serviceEntity.getId() );
        }
        serviceDto.setServiceName( serviceEntity.getServiceName() );
        serviceDto.setPrice( serviceEntity.getPrice() );
        serviceDto.setCommission( serviceEntity.getCommission() );
        serviceDto.setColorCode( serviceEntity.getColorCode() );
        serviceDto.setDescription( serviceEntity.getDescription() );
        if ( serviceEntity.getIsActive() != null ) {
            serviceDto.setIsActive( Boolean.parseBoolean( serviceEntity.getIsActive() ) );
        }

        return serviceDto;
    }

    @Override
    public ServiceEntity toServiceEntity(ServiceDto serviceDto) {
        if ( serviceDto == null ) {
            return null;
        }

        Long id = null;
        String serviceName = null;
        BigDecimal price = null;
        BigDecimal commission = null;
        String colorCode = null;
        String description = null;
        String isActive = null;

        id = serviceDto.getId();
        serviceName = serviceDto.getServiceName();
        price = serviceDto.getPrice();
        commission = serviceDto.getCommission();
        colorCode = serviceDto.getColorCode();
        description = serviceDto.getDescription();
        if ( serviceDto.getIsActive() != null ) {
            isActive = String.valueOf( serviceDto.getIsActive() );
        }

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
