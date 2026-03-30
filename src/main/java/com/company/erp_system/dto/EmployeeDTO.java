package com.company.erp_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "الاسم الأول مطلوب")
    @Size(min = 2, max = 50, message = "الاسم الأول يجب أن يكون بين 2 و 50 حرفاً")
    private String firstName;

    @NotBlank(message = "الاسم الأخير مطلوب")
    private String lastName;

    @NotBlank(message = "البريد الإلكتروني مطلوب")
    @Email(message = "صيغة البريد الإلكتروني غير صحيحة")
    private String email; // حقل أساسي لأي موظف

    private String phoneNumber;

    @NotNull(message = "يجب تحديد القسم الذي ينتمي إليه الموظف")
    private Long departmentId; // الربط مع القسم عبر الـ ID

    private String username;   // لربط الموظف بحساب مستخدم (اختياري)

    // --- Constructors ---
    public EmployeeDTO() {}

    public EmployeeDTO(Long id, String firstName, String lastName, String email, String phoneNumber, Long departmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
    }

    // --- Getters and Setters (وجودها هو ما يحل مشاكل الـ Service) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
