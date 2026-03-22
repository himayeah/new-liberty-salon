package com.bit.backend.services;

import java.util.List;

import com.bit.backend.dtos.SupplierDto;

public interface SupplierServiceI {

    SupplierDto addSupplier(SupplierDto supplierDto);

    List<SupplierDto> getSupplier();

    SupplierDto updateSupplier(long id, SupplierDto supplierDto);

    SupplierDto deleteSupplier(long id);

}
