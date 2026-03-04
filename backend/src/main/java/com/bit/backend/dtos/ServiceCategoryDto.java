package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryDto {
    private long id;
    private String categoryName;
    private Number displayOrder;
    private String description;

    public String getServiceName() {
        return categoryName;
    }
}
