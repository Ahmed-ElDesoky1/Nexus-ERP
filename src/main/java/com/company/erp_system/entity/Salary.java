package com.company.erp_system.entity;

import java.math.BigDecimal; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "salaries")
public class Salary extends BaseEntity { // الوراثة للحصول على الـ id والتواريخ تلقائياً

    @Column(nullable = false)
    private BigDecimal basicSalary;

    private BigDecimal bonuses;

    private BigDecimal deductions;

    @Column(nullable = false)
    private String month; 

    // الربط مع الموظف
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // --- Constructors ---
    public Salary() {}

    public Salary(BigDecimal basicSalary, String month, Employee employee) {
        this.basicSalary = basicSalary;
        this.month = month;
        this.employee = employee;
        this.bonuses = BigDecimal.ZERO;
        this.deductions = BigDecimal.ZERO;
    }

   

    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { this.basicSalary = basicSalary; }

    public BigDecimal getBonuses() { return bonuses; }
    public void setBonuses(BigDecimal bonuses) { this.bonuses = bonuses; }

    public BigDecimal getDeductions() { return deductions; }
    public void setDeductions(BigDecimal deductions) { this.deductions = deductions; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}
