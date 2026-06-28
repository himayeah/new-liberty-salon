package com.bit.backend;

import java.sql.*;

public class DBTest {
    public static void runTest() {
        String url = "jdbc:mysql://localhost:3306/ems?useSSL=false";
        String user = "root";
        String password = "Himaya123";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("CONNECTED TO DATABASE!");
            String sql = "SELECT YEAR(registration_date) AS registration_year, " +
                         "DATE_FORMAT(registration_date, '%M') AS registration_month, " +
                         "SUM(CASE WHEN gender = 'Male' THEN 1 ELSE 0 END) AS total_male_registrations, " +
                         "SUM(CASE WHEN gender = 'Female' THEN 1 ELSE 0 END) AS total_female_registrations " +
                         "FROM client_registration " +
                         "WHERE registration_date >= CURRENT_DATE() - INTERVAL 12 MONTH " +
                         "GROUP BY YEAR(registration_date), MONTH(registration_date), DATE_FORMAT(registration_date, '%M') " +
                         "ORDER BY YEAR(registration_date), MONTH(registration_date)";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                System.out.println("QUERY EXECUTED SUCCESSFULLY!");
                while (rs.next()) {
                    System.out.printf("Row: Year=%d, Month=%s, Male=%d, Female=%d%n",
                            rs.getInt(1), rs.getString(2), rs.getLong(3), rs.getLong(4));
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTest();
    }
}
