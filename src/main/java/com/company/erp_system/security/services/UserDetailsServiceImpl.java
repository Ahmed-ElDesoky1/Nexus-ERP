package com.company.erp_system.security.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.erp_system.entity.User;
import com.company.erp_system.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional // ضرورية لأن جلب الأدوار (Roles) يتطلب جلسة قاعدة بيانات مفتوحة (Lazy Loading)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. البحث عن المستخدم في قاعدة البيانات
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // 2. تحويل الأدوار (Roles) إلى SimpleGrantedAuthority
        // التعديل: نضمن أن النص المستخرج هو المسمى الكامل للدور (مثل ROLE_ADMIN)
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) 
                .collect(Collectors.toList());

        // 3. بناء كائن UserDetails الذي يستخدمه Spring Security في عمليات التحقق
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
