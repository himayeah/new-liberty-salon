package com.bit.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bit.backend.dtos.BillingDto;

@Service
public interface BillingService {

    BillingDto addBilling(BillingDto billingDto);

    List<BillingDto> getBilling();

    BillingDto deleteBilling(long id);

}
