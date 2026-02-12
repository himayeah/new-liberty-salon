package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.TaxDto;

public interface TaxServiceI {

    TaxDto addTax(TaxDto taxDto);

    List<TaxDto> getData();

    TaxDto updateTax(long id, TaxDto taxDto);

    TaxDto deleteTax(long id);

}
