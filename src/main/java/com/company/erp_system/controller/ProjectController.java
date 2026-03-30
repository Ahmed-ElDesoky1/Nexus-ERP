package com.company.erp_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // استيراد ضروري
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.erp_system.entity.Project;
import com.company.erp_system.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // 1. عرض كل المشاريع: مسموح للموظف والمدير (للمتابعة)
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Project> getAll() {
        return projectService.getAllProjects();
    }

    // 2. عرض مشروع واحد بالـ ID: مسموح للموظف والمدير
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // 3. إضافة مشروع جديد: للمدير فقط
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    // 4. تحديث بيانات مشروع: للمدير فقط
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project projectDetails) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDetails));
    }

    // 5. ربط موظف بمشروع: للمدير فقط (صلاحية إدارية بحتة)
    @PostMapping("/{projectId}/assign/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assign(@PathVariable Long projectId, @PathVariable Long employeeId) {
        projectService.assignEmployeeToProject(projectId, employeeId);
        return ResponseEntity.ok("تم ربط الموظف بالمشروع بنجاح!");
    }

    // 6. حذف مشروع: للمدير فقط
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // === الإضافات الجديدة لوحدة التقارير والتحليلات ===

    // 7. إحصائيات عامة عن حالات المشاريع: للمدير فقط
    @GetMapping("/reports/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getProjectStats() {
        return ResponseEntity.ok(projectService.getProjectStatistics());
    }

    // 8. تقرير تقدم المشاريع ونسبة الإنجاز: للمدير فقط
    @GetMapping("/reports/progress")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getProjectsProgress() {
        return ResponseEntity.ok(projectService.getProjectsProgressReport());
    }
}
