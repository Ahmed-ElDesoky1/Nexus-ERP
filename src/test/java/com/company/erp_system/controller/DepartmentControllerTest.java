package com.company.erp_system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.company.erp_system.dto.DepartmentDTO;
import com.company.erp_system.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false) // لتخطي جدار الحماية والتركيز على الـ API
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("اختبار جلب جميع الأقسام - نجاح")
    void getAllDepartments_Success() throws Exception {
        // Given
        when(departmentService.getAllDepartments()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/departments"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("اختبار إنشاء قسم جديد - نجاح")
    void createDepartment_Success() throws Exception {
        // Given
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("HR");
        dto.setDescription("Human Resources Department");

        when(departmentService.createDepartment(any(DepartmentDTO.class))).thenReturn(dto);

        // When & Then
        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    @DisplayName("اختبار تعيين مدير للقسم - نجاح")
    void assignManager_Success() throws Exception {
        // Given
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("IT");
        dto.setManagerName("Ahmed Ali");

        when(departmentService.assignManager(anyLong(), anyLong())).thenReturn(dto);

        // When & Then (محاكاة طلب PUT للرابط الخاص بتعيين المدير)
        mockMvc.perform(put("/api/departments/1/assign-manager/10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.managerName").value("Ahmed Ali"));
    }
}
