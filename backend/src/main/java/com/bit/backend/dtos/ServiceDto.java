package com.bit.backend.dtos;

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
    private BigDecimal price;
    private BigDecimal commission;
    private String colorCode;
    private String description;
    private Boolean isActive;
}
