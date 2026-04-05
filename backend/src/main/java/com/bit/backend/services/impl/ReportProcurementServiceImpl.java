package com.bit.backend.services.impl;

import com.bit.backend.dtos.ReportProcurementDto;
import com.bit.backend.mappers.PurchaseOrderMapper;
import com.bit.backend.repositories.PurchaseOrderDetailRepository;
import com.bit.backend.repositories.PurchaseOrderRepository;
import com.bit.backend.services.ReportProcurementService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ReportProcurementServiceImpl implements ReportProcurementService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public ReportProcurementServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    // The method that's being called by the Controller
    public List<ReportProcurementDto> getPendingPurchaseOrders() {

        // /calls the getPendingPurchaseOrders() method inside the Repository and store
        // the result in 'results' list
        List<Object[]> results = purchaseOrderRepository.getPendingPurchaseOrders();

        List<ReportProcurementDto> dtoList = new ArrayList<>();

        for (Object[] row : results) {

            ReportProcurementDto dto = new ReportProcurementDto();

            dto.setOrderNumber(row[0] != null ? row[0].toString() : null);
            dto.setSupplier(row[1] != null ? row[1].toString() : null);
            dto.setOrderDate(row[2] != null ? row[2].toString() : null);
            dto.setExpectedDate(row[3] != null ? row[3].toString() : null);
            dto.setLateDays(row[4] != null ? row[4].toString() : null);
            dto.setTotalWorth(row[5] != null ? row[5].toString() : null);

            dtoList.add(dto);
        }

        return dtoList;

    }

}
