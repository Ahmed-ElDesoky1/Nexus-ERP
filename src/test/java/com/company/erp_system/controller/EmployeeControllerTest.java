package com.company.erp_system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.company.erp_system.dto.EmployeeDTO;
import com.company.erp_system.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false) 
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("اختبار جلب جميع الموظفين ")
    void getAllEmployees_Success() throws Exception {
        when(employeeService.getAllEmployees(any(), any()))
            .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/employees"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("اختبار إضافة موظف جديد ببيانات كاملة ")
    void createEmployee_Success() throws Exception {
        // Given: ملء كل الحقول الإجبارية لتخطي الـ Validation
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("Sami");
        dto.setLastName("Ali");               
        dto.setEmail("sami@erp.com");        
        dto.setDepartmentId(1L);          

        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(dto);

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated()) 
               .andExpect(jsonPath("$.firstName").value("Sami"));
    }
}
