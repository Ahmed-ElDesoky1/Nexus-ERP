package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.company.erp_system.dto.SignupRequest;
import com.company.erp_system.entity.ERole;
import com.company.erp_system.entity.Role;
import com.company.erp_system.repository.RoleRepository;
import com.company.erp_system.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("اختبار نجاح تسجيل مستخدم جديد بأدوار صحيحة")
    void registerUser_Success() {
        // 1. Given: تجهيز طلب تسجيل
        SignupRequest request = new SignupRequest();
        request.setUsername("new_user");
        request.setEmail("user@erp.com");
        request.setPassword("123456");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        request.setRoles(roles);

        Role adminRole = new Role(ERole.ROLE_ADMIN);

        when(userRepository.existsByUsername("new_user")).thenReturn(false);
        when(userRepository.existsByEmail("user@erp.com")).thenReturn(false);
        when(encoder.encode("123456")).thenReturn("encoded_pass");
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        // 2. When: تنفيذ عملية التسجيل
        String response = authService.registerUser(request);

        // 3. Then: التحقق من النتيجة
        assertThat(response).isEqualTo("User registered successfully!");
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("اختبار فشل التسجيل بسبب تكرار اسم المستخدم")
    void registerUser_UsernameTaken_ReturnsError() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setUsername("existing_user");

        when(userRepository.existsByUsername("existing_user")).thenReturn(true);

        // When
        String response = authService.registerUser(request);

        // Then
        assertThat(response).isEqualTo("Error: Username is already taken!");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("اختبار رمي Exception عند عدم وجود الدور (Role) في قاعدة البيانات")
    void registerUser_RoleNotFound_ThrowsException() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setUsername("test_user");
        Set<String> roles = new HashSet<>();
        roles.add("manager");
        request.setRoles(roles);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // محاكاة عدم وجود الدور MANAGER في السيستم
        when(roleRepository.findByName(ERole.ROLE_MANAGER)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.registerUser(request));
    }
}
