package com.bit.backend.services;

import com.bit.backend.dtos.GrnDto;
import java.util.List;

public interface GrnService {
    List<GrnDto> getAllGrn();

    List<GrnDto> getGrnByPurchaseOrderId(Long purchaseOrderId);

    GrnDto saveGrn(GrnDto dto);

    GrnDto updateGrn(Long id, GrnDto dto);

    void deleteGrn(Long id);

    GrnDto getGrnByPurchaseOrderId(long purchaseOrderId);
}
