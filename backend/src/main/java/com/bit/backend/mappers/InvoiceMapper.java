package com.bit.backend.mappers;

import com.bit.backend.dtos.InvoiceDto;
import com.bit.backend.entities.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {InvoiceItemMapper.class})
public interface InvoiceMapper {
    @Mapping(source = "billing.id", target = "billingId")
    InvoiceDto toInvoiceDto(InvoiceEntity entity);

    @Mapping(source = "billingId", target = "billing.id")
    InvoiceEntity toInvoiceEntity(InvoiceDto dto);

    List<InvoiceDto> toInvoiceDtoList(List<InvoiceEntity> entities);
}
