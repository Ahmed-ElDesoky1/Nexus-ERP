package com.company.erp_system.service;

import java.util.List;

import com.company.erp_system.dto.DepartmentDTO;

public interface DepartmentService {

    // 1. دالة لإنشاء قسم جديد
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    // 2. دالة لجلب قائمة بكل الأقسام
    List<DepartmentDTO> getAllDepartments();

    // 3. دالة لجلب قسم واحد عن طريق الـ ID
    DepartmentDTO getDepartmentById(Long id);

    // 4. دالة لتحديث بيانات قسم موجود
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);

    // 5. دالة لحذف قسم
    void deleteDepartment(Long id);

    DepartmentDTO assignManager(Long deptId, Long empId);
}
