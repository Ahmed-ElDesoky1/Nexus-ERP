package com.company.erp_system.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.company.erp_system.entity.ERole;
import com.company.erp_system.entity.Role;
import com.company.erp_system.entity.User;
import com.company.erp_system.repository.RoleRepository;
import com.company.erp_system.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. إنشاء الأدوار إذا لم تكن موجودة
        createRoleIfNotFound(ERole.ROLE_ADMIN);
        createRoleIfNotFound(ERole.ROLE_EMPLOYEE);

        // 2. إنشاء مستخدم Admin مشفر (كلمة المرور: admin123)
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@erp.com");
            admin.setPassword(encoder.encode("admin123"));

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);

            System.out.println("**************************************************");
            System.out.println("===> SUCCESS: Default Admin (admin/admin123) Created!");
            System.out.println("**************************************************");
        }
    }

    private void createRoleIfNotFound(ERole roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
