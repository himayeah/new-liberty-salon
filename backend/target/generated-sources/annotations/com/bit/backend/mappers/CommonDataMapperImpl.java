package com.bit.backend.mappers;

import com.bit.backend.dtos.CommonDataDto;
import com.bit.backend.entities.CommonDataEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-27T23:06:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class CommonDataMapperImpl implements CommonDataMapper {

    @Override
    public CommonDataEntity toCommonDataEntity(CommonDataDto commonDataDto) {
        if ( commonDataDto == null ) {
            return null;
        }

        CommonDataEntity commonDataEntity = new CommonDataEntity();

        commonDataEntity.setId( commonDataDto.getId() );
        commonDataEntity.setDescription( commonDataDto.getDescription() );

        return commonDataEntity;
    }

    @Override
    public List<CommonDataEntity> toCommonDataEntityList(List<CommonDataDto> commonDataDtos) {
        if ( commonDataDtos == null ) {
            return null;
        }

        List<CommonDataEntity> list = new ArrayList<CommonDataEntity>( commonDataDtos.size() );
        for ( CommonDataDto commonDataDto : commonDataDtos ) {
            list.add( toCommonDataEntity( commonDataDto ) );
        }

        return list;
    }

    @Override
    public List<CommonDataDto> toCommonDataDtoList(List<CommonDataEntity> commonDataEntities) {
        if ( commonDataEntities == null ) {
            return null;
        }

        List<CommonDataDto> list = new ArrayList<CommonDataDto>( commonDataEntities.size() );
        for ( CommonDataEntity commonDataEntity : commonDataEntities ) {
            list.add( commonDataEntityToCommonDataDto( commonDataEntity ) );
        }

        return list;
    }

    protected CommonDataDto commonDataEntityToCommonDataDto(CommonDataEntity commonDataEntity) {
        if ( commonDataEntity == null ) {
            return null;
        }

        CommonDataDto commonDataDto = new CommonDataDto();

        commonDataDto.setId( commonDataEntity.getId() );
        commonDataDto.setDescription( commonDataEntity.getDescription() );

        return commonDataDto;
    }
}
