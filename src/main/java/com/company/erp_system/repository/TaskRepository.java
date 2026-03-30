package com.company.erp_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.Task;
import com.company.erp_system.entity.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 1. البحث عن المهام المسندة لموظف معين (باستخدام assignedTo الموجود في الـ Entity)
    List<Task> findByAssignedToId(Long employeeId);

    // 2. البحث عن المهام التابعة لمشروع معين
    List<Task> findByProjectId(Long projectId);

    // 3. البحث عن المهام حسب الحالة (TODO, COMPLETED,)
    List<Task> findByStatus(TaskStatus status);
}
