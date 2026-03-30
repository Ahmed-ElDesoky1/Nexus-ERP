package com.company.erp_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "اسم القسم مطلوب")
    private String name;

    @Size(max = 500, message = "وصف القسم لا يمكن أن يتجاوز 500 حرف")
    private String description;

    // --- حقول الربط الجديدة ---
    private Long managerId;      // لتعيين المدير عبر الـ ID
    private String managerName;  // لعرض اسم المدير في النتائج
    private int employeeCount;   // لعرض عدد الموظفين في القسم

    // --- Constructors ---
    public DepartmentDTO() {}

    public DepartmentDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public int getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(int employeeCount) { this.employeeCount = employeeCount; }
}
