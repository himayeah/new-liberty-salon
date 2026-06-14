package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee_reg")
public class EmployeeRegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "date_joined")
    private String dateJoined;

    @Column(name = "designation")
    private String designation;

    @Column(name = "specializations")
    private String specializations;

    @Column(name = "hourly_rate")
    private String hourlyRate;

    @Column(name = "commission_rate")
    private String commissionRate;

    @Column(name = "weekly_off_days")
    private String weeklyOffDays;

    @Column(name = "max_appointments_per_day")
    private String maxAppointmentsPerDay;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGTEXT")
    private String profileImage;

    @Column(name = "password")
    private String password;

    @Column(name = "invite_token")
    private String inviteToken;

    @Column(name = "reset_token")
    private String resetToken;

}
