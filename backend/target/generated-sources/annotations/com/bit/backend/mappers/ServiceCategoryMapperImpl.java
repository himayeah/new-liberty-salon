package com.bit.backend.mappers;

import com.bit.backend.dtos.ServiceCategoryDto;
import com.bit.backend.entities.ServiceCategoryEntity;
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

        Long id = null;
        String categoryName = null;
        Number displayOrder = null;
        String description = null;

        id = serviceCategoryDto.getId();
        categoryName = serviceCategoryDto.getCategoryName();
        displayOrder = serviceCategoryDto.getDisplayOrder();
        description = serviceCategoryDto.getDescription();

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
