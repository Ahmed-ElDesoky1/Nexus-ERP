package com.company.erp_system.service;

import java.util.List;
import java.util.Map;

import com.company.erp_system.entity.Task;
import com.company.erp_system.entity.TaskStatus;

public interface TaskService {

    // العمليات الأساسية للمهام
    Task createTask(Task task);

    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task updateTaskStatus(Long taskId, TaskStatus status);

    void assignTaskToEmployee(Long taskId, Long employeeId);

    void deleteTask(Long id);

 

    // 1.  قائمة المهام المتأخرة (التي تجاوزت تاريخ التسليم ولم تنتهِ)
    List<Task> getOverdueTasks();

    // 2.  إحصائيات عامة عن المهام (المكتملة، قيد التنفيذ، المتأخرة)
    Map<String, Object> getTaskCompletionStats();
}
