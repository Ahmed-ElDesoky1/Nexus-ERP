package com.company.erp_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // هذه الدالة التي يحتاجها الـ Service للفحص
    boolean existsByName(String name);
}
