package com.bit.backend.mappers;

import com.bit.backend.dtos.InvoiceItemDto;
import com.bit.backend.entities.InvoiceItemEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {
    InvoiceItemDto toDto(InvoiceItemEntity entity);
    InvoiceItemEntity toEntity(InvoiceItemDto dto);
    List<InvoiceItemDto> toDtoList(List<InvoiceItemEntity> entities);
    List<InvoiceItemEntity> toEntityList(List<InvoiceItemDto> dtos);
}
