package com.company.erp_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // البحث عن المشاريع التابعة لقسم معين باستخدام اسم القسم
    List<Project> findByDepartmentName(String departmentName);

    // البحث عن المشاريع حسب حالتها (مثلاً: "ACTIVE", "COMPLETED")
    List<Project> findByStatus(String status);

    // البحث عن المشاريع التي تحتوي على كلمة معينة في اسمها (Search functionality)
    List<Project> findByNameContainingIgnoreCase(String name);
}
