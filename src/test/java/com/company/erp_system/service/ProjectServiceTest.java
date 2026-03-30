package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.erp_system.entity.Employee;
import com.company.erp_system.entity.Project;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("اختبار حساب إحصائيات المشاريع بدقة")
    void getProjectStatistics_ShouldCalculateCorrectly() {
        // Given: تجهيز 3 مشاريع بحالات مختلفة
        Project p1 = new Project(); p1.setStatus("COMPLETED");
        Project p2 = new Project(); p2.setStatus("IN_PROGRESS");
        Project p3 = new Project(); p3.setStatus("IN_PROGRESS");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        // When: طلب الإحصائيات
        Map<String, Object> stats = projectService.getProjectStatistics();

        // Then: التحقق من الأرقام
        assertThat(stats.get("totalProjects")).isEqualTo(3);
        assertThat(stats.get("completedCount")).isEqualTo(1L);
        assertThat(stats.get("inProgressCount")).isEqualTo(2L);
    }

    @Test
    @DisplayName("اختبار نجاح تعيين موظف لمشروع")
    void assignEmployeeToProject_Success() {
        // Given
        Long projectId = 1L;
        Long empId = 10L;

        Project project = new Project();
        project.setId(projectId);
        project.setEmployees(new HashSet<>()); // قائمة فارغة

        Employee employee = new Employee();
        employee.setId(empId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));

        // When
        projectService.assignEmployeeToProject(projectId, empId);

        // Then
        assertThat(project.getEmployees()).contains(employee); // التأكد من الإضافة
        verify(projectRepository).save(project); // التأكد من حفظ التغيير
    }

    @Test
    @DisplayName("اختبار فشل جلب مشروع غير موجود")
    void getProjectById_NotFound_ThrowsException() {
        // Given
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.getProjectById(99L));
    }
}
