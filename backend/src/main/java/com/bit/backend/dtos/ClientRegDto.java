package com.bit.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientRegDto {

    // test update
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String preferredStylist;
    private String allergies;
    private int totalVisits;
    private String lastVisitedDate;
    private double lifetimeValue;
    private String photo;
    private String password;
    private String confirmPassword;
    private String resetToken;

    // Fields for Reports
    private Integer registrationYear;
    private String registrationMonth;
    private Long totalRegistrations;
    private Long totalFemaleRegistrations;
    private Long totalMaleRegistrations;
    private String clientAgeGroup;
    private Integer totalClientCount;
}
