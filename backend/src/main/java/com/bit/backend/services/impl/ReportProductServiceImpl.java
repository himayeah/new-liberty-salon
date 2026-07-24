package com.bit.backend.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bit.backend.dtos.ReportProductSalesDto;
import com.bit.backend.exceptions.AppException;
import com.bit.backend.repositories.BillingRepository;
import com.bit.backend.services.ReportProductSalesService;

@Service
public class ReportProductServiceImpl implements ReportProductSalesService {

    private final BillingRepository billingRepository;

    public ReportProductServiceImpl(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    // NEWLYADDED
    @Override
    public List<ReportProductSalesDto> getProductSalesData() {
        try {
            List<Object[]> rows = billingRepository.getProductSalesData();
            System.out.println("Data from Billing Repository:" + rows);
            List<ReportProductSalesDto> list = new ArrayList<>();
            for (Object[] row : rows) {
                ReportProductSalesDto reportProductSalesDto = new ReportProductSalesDto();
                reportProductSalesDto.setProductName((String) row[0]);
                reportProductSalesDto.setSoldQuantity(((Number) row[1]).longValue());
                reportProductSalesDto.setRevenue(((Number) row[2]).doubleValue());
                list.add(reportProductSalesDto);
                System.out.println("Produuct Sales DTO after Data:" + reportProductSalesDto);
            }

            return list;

        } catch (Exception e) {
            throw new AppException("Request failed with error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
