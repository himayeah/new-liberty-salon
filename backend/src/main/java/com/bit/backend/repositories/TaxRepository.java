package com.bit.backend.repositories;

import com.bit.backend.entities.TaxEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<TaxEntity, Long> {
}
