package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee_schedule")
public class EmployeeScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeRegEntity employeeRegEntity;

    @Column(name = "work_day")
    private Integer workDay;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "effective_date")
    private String effectiveDate;

    @Column(name = "end_date")
    private String endDate;
}
