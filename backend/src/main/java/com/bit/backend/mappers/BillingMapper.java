package com.bit.backend.mappers;

import com.bit.backend.dtos.BillingDto;
import com.bit.backend.entities.BillingEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BillingPurchaseMapper.class})
public interface BillingMapper {
    BillingDto toBillingDto(BillingEntity entity);
    BillingEntity toBillingEntity(BillingDto dto);
    List<BillingDto> toBillingDtoList(List<BillingEntity> entities);
}
