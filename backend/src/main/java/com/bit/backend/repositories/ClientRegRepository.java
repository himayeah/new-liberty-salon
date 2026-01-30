package com.bit.backend.repositories;

import com.bit.backend.entities.ClientRegEntity;
    import org.springframework.data.jpa.repository.JpaRepository;

//mention where you named the database Table(ClientRegEntity) and the table's id type (Long)
public interface ClientRegRepository extends JpaRepository <ClientRegEntity, Long> {
}
