package com.company.erp_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.erp_system.dto.EmployeeDTO;

/**
 * واجهة خدمة الموظفين - تحدد العمليات الأساسية المطلوبة
 */
public interface EmployeeService {

    /**
     * جلب قائمة الموظفين مع دعم البحث والترقيم
     * التحسين: التعامل مع search كمعامل اختياري
     */
    Page<EmployeeDTO> getAllEmployees(String search, Pageable pageable);

    /**
     * جلب بيانات موظف معين
     * التحسين: التأكد من التعامل مع حالة عدم الوجود (EntityNotFound)
     */
    EmployeeDTO getEmployeeById(Long id);

    /**
     * إضافة موظف جديد وربطه بالقسم والمستخدم (Linking)
     * هذا يحقق "ربط الموظف بالقسم والمستخدم" كما في طلبك الأول
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    /**
     * تحديث بيانات موظف موجود
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    /**
     * حذف موظف نهائياً من النظام
     */
    void deleteEmployee(Long id);

    //  إضافة دالة للتحقق من وجود الإيميل
    boolean existsByEmail(String email);
}
