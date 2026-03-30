package com.company.erp_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.ERole;
import com.company.erp_system.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // البحث عن الدور باستخدام الـ Enum (مثل ROLE_ADMIN)
    Optional<Role> findByName(ERole name);
}

