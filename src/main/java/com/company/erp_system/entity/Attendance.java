package com.company.erp_system.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity {


    @Column(name = "date", nullable = false)
    private LocalDate date;      // تاريخ اليوم

    @Column(name = "check_in_time")
    private LocalTime checkIn;   // وقت الحضور

    @Column(name = "check_out_time")
    private LocalTime checkOut;  // وقت الانصراف

    @Column(name = "status")
    private String status;       // PRESENT, ABSENT, LATE, EXCUSED

    @Column(name = "total_hours")
    private Double totalHours;   // عدد الساعات المنجزة

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    // --- Constructors ---
    public Attendance() {}

    public Attendance(LocalDate date, LocalTime checkIn, Employee employee) {
        this.date = date;
        this.checkIn = checkIn;
        this.employee = employee;
        this.status = "PRESENT";
    }

    // --- Getters and Setters ---
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }

    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}
