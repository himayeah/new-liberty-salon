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

        @Query(value = "SELECT YEAR(registration_date) AS registration_year, " +
                        "DATE_FORMAT(registration_date, '%M') AS registration_month, " +
                        "SUM(CASE WHEN gender = 'Male' THEN 1 ELSE 0 END) AS total_male_registrations, " +
                        "SUM(CASE WHEN gender = 'Female' THEN 1 ELSE 0 END) AS total_female_registrations " +
                        "FROM client_registration " +
                        "WHERE registration_date >= CURRENT_DATE() - INTERVAL 12 MONTH " +
                        "GROUP BY YEAR(registration_date), MONTH(registration_date), DATE_FORMAT(registration_date, '%M') "
                        +
                        "ORDER BY YEAR(registration_date), MONTH(registration_date)", nativeQuery = true)
        List<Object[]> getRegistrationsByGender();

        // For Public Booking Login
        // Can be written using @Query as well, but Query is mostly used for complex
        // logics. and commonly used for Reporting etc.
        // Java automatically understands the logic for simple queries like this.
        java.util.Optional<ClientRegEntity> findByFirstNameAndEmail(String firstName, String email);

}
