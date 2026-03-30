package com.company.erp_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // إضافة التشفير
import org.springframework.stereotype.Service;

import com.company.erp_system.entity.User;
import com.company.erp_system.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    // دالة لجلب جميع المستخدمين
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // دالة لحفظ مستخدم جديد (مع تشفير كلمة المرور)
    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //  للبحث عن مستخدم بالاسم
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
