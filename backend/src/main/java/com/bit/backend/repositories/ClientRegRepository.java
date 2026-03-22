package com.bit.backend.repositories;

import com.bit.backend.entities.ClientRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//mention where you named the database Table(ClientRegEntity) and the table's id type (Long)
public interface ClientRegRepository extends JpaRepository<ClientRegEntity, Long> {

    @Query(value = "SELECT YEAR(registration_date) as registrationYear, COUNT(*) as totalRegistrations " +
            "FROM client_registration " +
            "WHERE registration_date IS NOT NULL " +
            "GROUP BY YEAR(registration_date) " +
            "ORDER BY YEAR(registration_date)", nativeQuery = true)
    List<Object[]> countRegistrationsByYear();

    // Dashboard card (New Clients within last 30 days)
    @Query(value = "SELECT COUNT(*) FROM client_registration WHERE registration_date >= CURRENT_DATE - INTERVAL 30 DAY", nativeQuery = true)
    long countClientRegistrationsLast30Days();

}
