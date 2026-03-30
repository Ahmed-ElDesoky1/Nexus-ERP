package com.company.erp_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * البحث عن المستخدم بواسطة اسم المستخدم.
     * تُستخدم هذه الدالة بواسطة Spring Security للتحقق من بيانات الدخول.
     */
    Optional<User> findByUsername(String username);

    /**
     * التأكد من وجود اسم المستخدم مسبقاً.
     * تُستخدم عند إضافة موظف جديد لمنع تكرار أسماء المستخدمين.
     */
    boolean existsByUsername(String username);

    /**
     * التأكد من وجود البريد الإلكتروني مسبقاً.
     * تضمن عدم تسجيل أكثر من حساب بنفس البريد الإلكتروني.
     */
    boolean existsByEmail(String email);
}
