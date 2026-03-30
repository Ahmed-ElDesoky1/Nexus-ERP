package com.company.erp_system.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. دالة البحث والترقيم (التي أرسلتها أنت - ممتازة)
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );

    // 2. إضافة: البحث عن موظف بواسطة البريد الإلكتروني (مهم جداً للتحقق قبل الحفظ)
    Optional<Employee> findByEmail(String email);

    // 3. إضافة (اختيارية): البحث عن الموظفين حسب اسم القسم المرتبط بهم
    Page<Employee> findByDepartmentNameContainingIgnoreCase(String deptName, Pageable pageable);

    // 4. إضافة (اختيارية): التأكد من وجود إيميل مسبقاً (ترجع true أو false)
    boolean existsByEmail(String email);
}
