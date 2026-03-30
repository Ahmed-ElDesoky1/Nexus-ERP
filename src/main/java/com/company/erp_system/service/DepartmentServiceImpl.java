package com.company.erp_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.erp_system.dto.DepartmentDTO;
import com.company.erp_system.entity.Department;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.repository.DepartmentRepository;
import com.company.erp_system.repository.EmployeeRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository; // إضافة Repository الموظفين للربط

    // Constructor Injection لكل من مستودع الأقسام والموظفين
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new RuntimeException("عذراً، هذا القسم موجود بالفعل!");
        }

        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());

        Department savedDept = departmentRepository.save(department);
        return mapToDTO(savedDept);
    }

    // --- المهمة: تعيين مدير للقسم (Assign Manager) ---
    @Override
    @Transactional
    public DepartmentDTO assignManager(Long deptId, Long empId) {
        Department dept = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("القسم غير موجود"));

        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("الموظف المراد تعيينه كمدير غير موجود"));

        dept.setManager(emp); // ربط الموظف كمدير للقسم
        return mapToDTO(departmentRepository.save(dept));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("القسم غير موجود بالرقم: " + id));
        return mapToDTO(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("لا يمكن الحذف، القسم غير موجود!");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("عذراً، القسم غير موجود لتحديثه"));

        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());

        return mapToDTO(departmentRepository.save(department));
    }

    // --- تحسين دالة التحويل (Mapping) لتشمل بيانات المدير وعدد الموظفين ---
    private DepartmentDTO mapToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());

        // 1. جلب بيانات المدير إذا كان موجوداً
        if (department.getManager() != null) {
            dto.setManagerId(department.getManager().getId());
            dto.setManagerName(department.getManager().getFirstName() + " " + department.getManager().getLastName());
        }

        // 2. حساب عدد الموظفين التابعين لهذا القسم (المهمة الثالثة في الصورة)
        if (department.getEmployees() != null) {
            dto.setEmployeeCount(department.getEmployees().size());
        } else {
            dto.setEmployeeCount(0);
        }

        return dto;
    }
}
