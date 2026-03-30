package com.company.erp_system.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.company.erp_system.entity.User;
import com.company.erp_system.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("اختبار نجاح استرجاع بيانات المستخدم")
    void loadUserByUsername_Success() {
        // 1. Given: تجهيز مستخدم وهمي
        User mockUser = new User();
        mockUser.setUsername("admin");
        mockUser.setPassword("123456");
        mockUser.setRoles(Collections.emptySet());

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockUser));

        // 2. When: تشغيل الدالة
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        // 3. Then: التحقق من النتيجة
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin");
    }

    @Test
    @DisplayName("اختبار فشل العملية عند عدم وجود المستخدم")
    void loadUserByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
    }
}
