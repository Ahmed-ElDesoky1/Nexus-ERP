package com.company.erp_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    // 1. وصول عام: متاح للجميع
    @GetMapping("/all")
    public String allAccess() {
        return "محتوى عام: متاح للجميع (Welcome to ERP System).";
    }

    // 2. وصول الموظفين: تم تعديل USER إلى EMPLOYEE لتطابق قاعدة بياناتك
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String userAccess() {
        return "محتوى الموظفين: متاح لكل من لديه حساب فعال.";
    }

    // 3. لوحة تحكم المدير: محصورة بالـ ADMIN فقط
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "لوحة تحكم المدير: وصول كامل ومطلق للإعدادات!";
    }
}
