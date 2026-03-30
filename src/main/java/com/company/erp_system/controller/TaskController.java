package com.company.erp_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // استيراد ضروري
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.erp_system.entity.Task;
import com.company.erp_system.entity.TaskStatus;
import com.company.erp_system.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 1. عرض كل المهام: مسموح للموظف والمدير
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    // 2. عرض مهمة واحدة بالـ ID: مسموح للموظف والمدير
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // 3. إضافة مهمة جديدة: للمدير فقط
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    // 4. تحديث حالة المهمة: مسموح للموظف (لتحديث تقدمه) وللمدير
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    // 5. إسناد المهمة لموظف: للمدير فقط
    @PostMapping("/{taskId}/assign/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assign(@PathVariable Long taskId, @PathVariable Long employeeId) {
        taskService.assignTaskToEmployee(taskId, employeeId);
        return ResponseEntity.ok("تم إسناد المهمة للموظف بنجاح!");
    }

    // === الإضافات الجديدة لوحدة التقارير والتحليلات ===

    // 6. تقرير المهام المتأخرة: للمدير فقط (لأغراض المتابعة والمحاسبة)
    @GetMapping("/reports/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    // 7. إحصائيات إنجاز المهام: للمدير فقط
    @GetMapping("/reports/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getTaskStats() {
        return ResponseEntity.ok(taskService.getTaskCompletionStats());
    }
}
