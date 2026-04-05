package com.bit.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDto {
    private long id;
    private String serviceName;
    private Integer duration;
    private BigDecimal price;
    private BigDecimal commission;
    private String colorCode;
    private String description;
    @JsonProperty("is_active")
    private Boolean isActive;
    private ServiceCategoryDto serviceCategory;

    public String getServiceName() {
        return serviceName;
    }
}
