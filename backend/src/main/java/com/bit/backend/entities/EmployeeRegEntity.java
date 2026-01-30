package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="employee_reg")
public class EmployeeRegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="employee_name")
    private String employeeName;

    @Column(name="date_joined")
    private String dateJoined;

    @Column(name="designation")
    private String designation;

    @Column(name="specializations")
    private String specializations;

    @Column(name="hourly_rate")
    private String hourlyRate;

    @Column(name="commission_rate")
    private String commissionRate;

    @Column(name="weekly_off_days")
    private String weeklyOffDays;

    @Column(name="max_appointments_per_day")
    private String maxAppointmentsPerDay;

    @Column(name="actions")
    private String actions;

    public EmployeeRegEntity() {
    }

    public void setId(long id) {
    }
}
