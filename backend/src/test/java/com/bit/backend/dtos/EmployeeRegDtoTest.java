package com.bit.backend.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class EmployeeRegDtoTest {

    @Test
    void getIdShouldReturnTheStoredIdValue() {
        EmployeeRegDto dto = EmployeeRegDto.builder().id(42L).employeeName("Alice").build();

        assertEquals(42L, dto.getId());
    }
}
