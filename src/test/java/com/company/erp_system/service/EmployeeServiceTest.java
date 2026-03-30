package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.erp_system.dto.EmployeeDTO;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.repository.DepartmentRepository;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    
    @Mock
    private DepartmentRepository departmentRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("اختبار نجاح استرجاع موظف عن طريق الـ ID")
    void getEmployeeById_Success() {
        // 1. Given
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Ahmed");
        employee.setEmail("ahmed@erp.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // 2. When
        EmployeeDTO result = employeeService.getEmployeeById(1L);

        // 3. Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Ahmed");
        verify(employeeRepository).findById(1L);
    }

    @Test
    @DisplayName("اختبار فشل إضافة موظف بإيميل مكرر")
    void createEmployee_DuplicateEmail_ThrowsException() {
        // 1. Given
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmail("duplicate@erp.com");

        // محاكاة أن الإيميل موجود مسبقاً
        when(employeeRepository.existsByEmail("duplicate@erp.com")).thenReturn(true);

        // 2. When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.createEmployee(dto);
        });

        assertThat(exception.getMessage()).isEqualTo("البريد الإلكتروني مستخدم بالفعل!");
        verify(employeeRepository, never()).save(any()); // التأكد أن الحفظ لم يتم
    }

    @Test
    @DisplayName("اختبار حذف موظف غير موجود يرمي Exception")
    void deleteEmployee_NotFound_ThrowsException() {
        // Given
        when(employeeRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(99L);
        });
    }
}
