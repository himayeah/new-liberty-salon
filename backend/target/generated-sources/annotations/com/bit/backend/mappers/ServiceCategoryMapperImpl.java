package com.bit.backend.mappers;

import com.bit.backend.dtos.ServiceCategoryDto;
import com.bit.backend.entities.ServiceCategoryEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ServiceCategoryMapperImpl implements ServiceCategoryMapper {

    @Override
    public ServiceCategoryDto toServiceCategoryDto(ServiceCategoryEntity serviceCategoryEntity) {
        if ( serviceCategoryEntity == null ) {
            return null;
        }

        long id = 0L;
        String categoryName = null;
        Number displayOrder = null;
        String description = null;

        if ( serviceCategoryEntity.getId() != null ) {
            id = serviceCategoryEntity.getId();
        }
        categoryName = serviceCategoryEntity.getCategoryName();
        displayOrder = serviceCategoryEntity.getDisplayOrder();
        description = serviceCategoryEntity.getDescription();

        ServiceCategoryDto serviceCategoryDto = new ServiceCategoryDto( id, categoryName, displayOrder, description );

        return serviceCategoryDto;
    }

    @Override
    public ServiceCategoryEntity toServiceCategoryEntity(ServiceCategoryDto serviceCategoryDto) {
        if ( serviceCategoryDto == null ) {
            return null;
        }

        String categoryName = null;
        String description = null;
        Number displayOrder = null;
        Long id = null;

        categoryName = serviceCategoryDto.getCategoryName();
        description = serviceCategoryDto.getDescription();
        displayOrder = serviceCategoryDto.getDisplayOrder();
        id = serviceCategoryDto.getId();

        ServiceCategoryEntity serviceCategoryEntity = new ServiceCategoryEntity( id, categoryName, displayOrder, description );

        return serviceCategoryEntity;
    }

    @Override
    public List<ServiceCategoryDto> toServiceCategoryDtoList(List<ServiceCategoryEntity> serviceCategoryEntityList) {
        if ( serviceCategoryEntityList == null ) {
            return null;
        }

        List<ServiceCategoryDto> list = new ArrayList<ServiceCategoryDto>( serviceCategoryEntityList.size() );
        for ( ServiceCategoryEntity serviceCategoryEntity : serviceCategoryEntityList ) {
            list.add( toServiceCategoryDto( serviceCategoryEntity ) );
        }

        return list;
    }
}
