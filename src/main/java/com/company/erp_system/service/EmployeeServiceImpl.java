package com.company.erp_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.erp_system.dto.EmployeeDTO;
import com.company.erp_system.entity.Department;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.entity.User;
import com.company.erp_system.repository.DepartmentRepository;
import com.company.erp_system.repository.EmployeeRepository;
import com.company.erp_system.repository.UserRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    // Constructor Injection
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(String search, Pageable pageable) {
        Page<Employee> employees;
        if (search != null && !search.isEmpty()) {
            employees = employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, pageable);
        } else {
            employees = employeeRepository.findAll(pageable);
        }
        return employees.map(this::mapToDTO);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الموظف غير موجود برقم: " + id));
        return mapToDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        //  التأكد من عدم تكرار الإيميل قبل الحفظ
        if (existsByEmail(dto.getEmail())) {
            throw new RuntimeException("البريد الإلكتروني مستخدم بالفعل!");
        }

        Employee employee = new Employee();
        updateEmployeeFields(employee, dto);

        // ربط القسم
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("القسم غير موجود"));
            employee.setDepartment(dept);
        }

        // ربط المستخدم
        if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
            User user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));
            employee.setUser(user);
        }

        return mapToDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الموظف غير موجود"));

        updateEmployeeFields(employee, dto);

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("القسم غير موجود"));
            employee.setDepartment(dept);
        }

        return mapToDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("الموظف غير موجود");
        }
        employeeRepository.deleteById(id);
    }

  
    @Override
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

 

    private void updateEmployeeFields(Employee employee, EmployeeDTO dto) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }
        if (employee.getUser() != null) {
            dto.setUsername(employee.getUser().getUsername());
        }
        return dto;
    }
}
