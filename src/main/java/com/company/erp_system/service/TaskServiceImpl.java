package com.company.erp_system.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.erp_system.entity.Employee;
import com.company.erp_system.entity.Task;
import com.company.erp_system.entity.TaskStatus;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المهمة غير موجودة برقم: " + id));
    }

    @Override
    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = getTaskById(taskId);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void assignTaskToEmployee(Long taskId, Long employeeId) {
        Task task = getTaskById(taskId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("الموظف غير موجود برقم: " + employeeId));

        task.setAssignedTo(employee);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }


    @Override
    public List<Task> getOverdueTasks() {
        LocalDate today = LocalDate.now();
        //  المهام التي تجاوزت تاريخ الاستحقاق ولم تكتمل بعد
        return taskRepository.findAll().stream()
                .filter(task -> task.getDueDate() != null &&
                       task.getDueDate().isBefore(today) &&
                       !task.getStatus().name().equals("DONE") &&
                       !task.getStatus().name().equals("COMPLETED"))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getTaskCompletionStats() {
        List<Task> allTasks = taskRepository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", allTasks.size());

        // حساب المهام المكتملة بناءً على الاسم النصي لتجنب أخطاء الـ Enum
        long completed = allTasks.stream()
                .filter(t -> t.getStatus().name().equals("DONE") || t.getStatus().name().equals("COMPLETED"))
                .count();

        long inProgress = allTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
                .count();

        stats.put("completedCount", completed);
        stats.put("inProgressCount", inProgress);
        stats.put("overdueCount", getOverdueTasks().size());

        return stats;
    }
}
