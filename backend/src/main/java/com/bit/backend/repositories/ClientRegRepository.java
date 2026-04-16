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
    @Query(value = "SELECT COUNT(*) FROM client_registration WHERE registration_date BETWEEN CURRENT_DATE - INTERVAL 30 DAY AND CURRENT_DATE", nativeQuery = true)
    long countClientRegistrationsLast30Days();

    // Report client-registration (Female and Male registrations table)
    @Query(value = "SELECT DATE_FORMAT(c.registration_date, '%M') AS registration_month, " +
            "c.gender, " +
            "COUNT(c.id) AS total_registrations " +
            "FROM client_registration c " +
            "WHERE c.registration_date >= CURRENT_DATE() - INTERVAL 6 MONTH " +
            "GROUP BY registration_month, c.gender " +
            "ORDER BY registration_month, c.gender", nativeQuery = true)
    List<Object[]> getRegistrationsByGender();

}
