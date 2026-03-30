package com.company.erp_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // استيراد ضروري
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.erp_system.entity.User;
import com.company.erp_system.service.UserService;

@RestController
@RequestMapping("/api/users") // تم تعديله ليتناسب مع هيكلية المسارات لديك
@PreAuthorize("hasRole('ADMIN')") // حماية الكلاس بالكامل: للمدير فقط
public class UserController {

    @Autowired
    private UserService userService;

    // الحصول على كل المستخدمين: للمدير فقط
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // إضافة مستخدم جديد: للمدير فقط
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
