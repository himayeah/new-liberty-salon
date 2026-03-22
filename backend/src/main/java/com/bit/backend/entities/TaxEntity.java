package com.bit.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tax")
public class TaxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_name")
    private String taxName;

    @Column(name = "tax_rate")
    private String taxRate;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "is_active")
    private String isActive;

    public void setId(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
