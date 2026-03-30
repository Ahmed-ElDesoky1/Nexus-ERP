package com.company.erp_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING) // لتخزين الاسم كنص في قاعدة البيانات (ADMIN بدلاً من 0)
    @Column(length = 20, nullable = false, unique = true)
    private ERole name;

    // constructor فارغ لـ JPA
    public Role() {}

    // constructor لتسهيل إنشاء الأدوار
    public Role(ERole name) {
        this.name = name;
    }

    // Getters and Setters
    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
