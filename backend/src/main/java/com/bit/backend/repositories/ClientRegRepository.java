package com.bit.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bit.backend.entities.ClientRegEntity;

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

        // Client Registration By Age Groups (Pie Chart)
        // commas seperate selected columns
        // spaces seperate SQL keywords. If you write, AND 35 THEN '25-35'" + "WHEN
        // timestampdiff(YEAR.. then SQL joins them as;
        // AND 35 THEN '25-35'WHEN timestampdiff(YEAR..
        @Query(value = "SELECT CASE WHEN timestampdiff(YEAR, date_of_birth, CURRENT_DATE()) BETWEEN 15 AND 25 THEN '15-25' "
                        +
                        "WHEN timestampdiff(YEAR, date_of_birth, CURRENT_DATE()) BETWEEN 26 AND 35 THEN '25-35' " +
                        "WHEN timestampdiff(YEAR, date_of_birth, CURRENT_DATE()) BETWEEN 35 AND 45 THEN '35-45' " +
                        // Below line comma means, "I'm finished selecting the first column"
                        "END AS client_age_group, " +
                        "COUNT(*) AS total_client_count " +
                        "FROM client_registration " +
                        // date filter (All time, last 1 Month, last 3 Month, custom)
                        "WHERE registration_date BETWEEN :startDate AND :endDate " +
                        "GROUP BY client_age_group ", nativeQuery = true)
        List<Object[]> getRegistrationsByAgeGroup(@Param("startDate") String startDate,
                        @Param("endDate") String endDate);

        // The backend will return an Object Array like;
        // -----------------------------------------------
        // List<Object[]> results = [
        // ["15-25", 12],
        // ["25-35", 20],
        // ["35-45", 8]
        // ];

        // To access this object Array in the serviceImpl;
        // for (Object[] row = resul) {
        // System.out.println(row[0])
        // }

}
