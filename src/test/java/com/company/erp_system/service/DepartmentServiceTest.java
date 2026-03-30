package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.erp_system.dto.DepartmentDTO;
import com.company.erp_system.entity.Department;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.repository.DepartmentRepository;
import com.company.erp_system.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    @DisplayName("اختبار نجاح إنشاء قسم جديد")
    void createDepartment_Success() {
        // Given
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("IT");
        dto.setDescription("Information Technology");

        when(departmentRepository.existsByName("IT")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        DepartmentDTO result = departmentService.createDepartment(dto);

        // Then
        assertThat(result.getName()).isEqualTo("IT");
        verify(departmentRepository).save(any());
    }

    @Test
    @DisplayName("اختبار فشل إنشاء قسم باسم موجود مسبقاً")
    void createDepartment_DuplicateName_ThrowsException() {
        // Given
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("HR");

        when(departmentRepository.existsByName("HR")).thenReturn(true);

        // When & Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> departmentService.createDepartment(dto));
        assertThat(ex.getMessage()).isEqualTo("عذراً، هذا القسم موجود بالفعل!");
    }

    @Test
    @DisplayName("اختبار نجاح تعيين مدير للقسم")
    void assignManager_Success() {
        // Given
        Long deptId = 1L;
        Long empId = 10L;

        Department dept = new Department();
        dept.setId(deptId);
        dept.setName("Sales");

        Employee emp = new Employee();
        emp.setId(empId);
        emp.setFirstName("Sami");
        emp.setLastName("Ali");

        when(departmentRepository.findById(deptId)).thenReturn(Optional.of(dept));
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(emp));
        when(departmentRepository.save(any())).thenReturn(dept);

        // When
        DepartmentDTO result = departmentService.assignManager(deptId, empId);

        // Then
        assertThat(result.getManagerId()).isEqualTo(empId);
        assertThat(result.getManagerName()).isEqualTo("Sami Ali");
        verify(departmentRepository).save(dept);
    }
}
