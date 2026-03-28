package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "client_registration")
public class ClientRegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "preferred_stylist")
    private String preferredStylist;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "total_visits")
    private int totalVisits;

    @Column(name = "last_visited_date")
    private String lastVisitedDate;

    @Column(name = "lifetime_value")
    private double lifetimeValue;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGTEXT")
    private String photo;

    // client-reg report
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @PrePersist
    protected void onCreate() {
        if (this.registrationDate == null) {
            this.registrationDate = LocalDate.now();
        }
    }

}
