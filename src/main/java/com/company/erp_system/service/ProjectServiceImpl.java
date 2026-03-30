package com.company.erp_system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.erp_system.entity.Employee;
import com.company.erp_system.entity.Project;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على المشروع بالرقم: " + id));
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, Project projectDetails) {
        Project project = getProjectById(id);
        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setStartDate(projectDetails.getStartDate());
        project.setEndDate(projectDetails.getEndDate());
        project.setStatus(projectDetails.getStatus());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void assignEmployeeToProject(Long projectId, Long employeeId) {
        Project project = getProjectById(projectId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("لم يتم العثور على الموظف بالرقم: " + employeeId));

        project.getEmployees().add(employee);
        projectRepository.save(project);
    }

    @Override
    public Map<String, Object> getProjectStatistics() {
        List<Project> allProjects = projectRepository.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", allProjects.size());
        stats.put("completedCount", allProjects.stream().filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus())).count());
        stats.put("inProgressCount", allProjects.stream().filter(p -> "IN_PROGRESS".equalsIgnoreCase(p.getStatus())).count());
        stats.put("pendingCount", allProjects.stream().filter(p -> "PENDING".equalsIgnoreCase(p.getStatus())).count());

        return stats;
    }

    @Override
    public List<Map<String, Object>> getProjectsProgressReport() {
        return projectRepository.findAll().stream().map(project -> {
            Map<String, Object> report = new HashMap<>();
            report.put("projectId", project.getId());
            report.put("projectName", project.getName());
            report.put("progress", project.getCompletionPercentage() + "%");
            report.put("status", project.getStatus());
            return report;
        }).collect(Collectors.toList());
    }
}
