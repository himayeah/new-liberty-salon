package com.bit.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employee_leave")
public class EmployeeLeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="employee_name")
    private String employeeName;

    @Column(name="leave_type")
    private String leaveType;

    @Column(name="start_date")
    private String startDate;

    @Column(name="end_date")
    private String endDate;

    @Column(name="reason")
    private String reason;

    public void setId(long id) {
    }

    public EmployeeLeaveEntity(Long id, String employeeName, String leaveType, String startDate, String endDate, String reason) {
        this.id = id;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }
}
