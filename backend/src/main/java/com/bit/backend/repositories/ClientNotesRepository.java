package com.bit.backend.repositories;

import com.bit.backend.entities.ClientNotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientNotesRepository extends JpaRepository <ClientNotesEntity, Long> {
}
