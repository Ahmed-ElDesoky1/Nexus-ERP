package com.company.erp_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // استيراد ضروري
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.erp_system.service.HelloService;

@RestController
@RequestMapping("/api/hello") // تحديث المسار ليتماشى مع باقي النظام
public class HelloController {

    @Autowired
    private HelloService helloService;

    // 1. رسالة الترحيب: مسموح لأي مستخدم مسجل دخول
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String hello() {
        return helloService.getWelcomeMessage();
    }

    // 2. حالة النظام: للمدير فقط
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String status() {
        return helloService.getSystemStatus();
    }
}
