package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.erp_system.entity.Task;
import com.company.erp_system.entity.TaskStatus;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("اختبار جلب المهام المتأخرة (Overdue)")
    void getOverdueTasks_ShouldReturnCorrectTasks() {
        // Given: مهمة تاريخها قديم ولم تنتهِ، ومهمة أخرى مكتملة
        Task overdueTask = new Task();
        overdueTask.setDueDate(LocalDate.now().minusDays(5));
        overdueTask.setStatus(TaskStatus.IN_PROGRESS);

        Task completedTask = new Task();
        completedTask.setDueDate(LocalDate.now().minusDays(10));
        completedTask.setStatus(TaskStatus.COMPLETED); // تم استبدال DONE بـ COMPLETED

        when(taskRepository.findAll()).thenReturn(Arrays.asList(overdueTask, completedTask));

        // When
        List<Task> result = taskService.getOverdueTasks();

        // Then: يجب أن تعود المهمة المتأخرة فقط (لأن المكتملة لا تُعتبر متأخرة)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("اختبار إحصائيات إنجاز المهام")
    void getTaskCompletionStats_ShouldCalculateCorrectly() {
        // Given: تجهيز مجموعة مهام بحالات مختلفة
        Task t1 = new Task(); 
        t1.setStatus(TaskStatus.COMPLETED); // تم استبدال DONE بـ COMPLETED
        
        Task t2 = new Task(); 
        t2.setStatus(TaskStatus.IN_PROGRESS);
        
        Task t3 = new Task(); 
        t3.setStatus(TaskStatus.TODO);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2, t3));

        // When
        Map<String, Object> stats = taskService.getTaskCompletionStats();

        // Then
        assertThat(stats.get("totalTasks")).isEqualTo(3);
        assertThat(stats.get("completedCount")).isEqualTo(1L);
        assertThat(stats.get("inProgressCount")).isEqualTo(1L);
    }
}
