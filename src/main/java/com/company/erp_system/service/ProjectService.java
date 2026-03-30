package com.company.erp_system.service;

import java.util.List;
import java.util.Map;

import com.company.erp_system.entity.Project;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project createProject(Project project);
    Project updateProject(Long id, Project projectDetails);
    void deleteProject(Long id);

    // إضافة موظف لمشروع
    void assignEmployeeToProject(Long projectId, Long employeeId);

    

    // 1.  إحصائيات عامة عن حالات المشاريع 
    Map<String, Object> getProjectStatistics();

    // 2.  تقرير تفصيلي بنسبة إنجاز كل مشروع بناءً على مهامه
    List<Map<String, Object>> getProjectsProgressReport();
}
