package com.company.erp_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.erp_system.entity.Attendance;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.repository.AttendanceRepository;
import com.company.erp_system.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    @DisplayName("اختبار حساب ساعات العمل بدقة عند الانصراف")
    void checkOut_ShouldCalculateTotalHoursCorrectly() {
        Long empId = 1L;
        Attendance attendance = new Attendance();
        attendance.setCheckIn(LocalTime.of(8, 0)); // حضور 8 صباحاً
        
        when(attendanceRepository.findByEmployeeIdAndDate(eq(empId), any(LocalDate.class)))
            .thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Attendance result = attendanceService.checkOut(empId);

        assertThat(result.getCheckOut()).isNotNull();
        assertThat(result.getTotalHours()).isGreaterThan(0);
    }

    @Test
    @DisplayName("اختبار تقرير الأداء الشهري وحساب نسبة الانضباط")
    void getEmployeePerformanceReport_ShouldCalculateRateCorrectly() {
        // Given: موظف حضر يومين، أحدهما متأخر (LATE)
        Long empId = 1L;
        Attendance day1 = new Attendance();
        day1.setDate(LocalDate.of(2024, 3, 1));
        day1.setStatus("PRESENT");
        day1.setTotalHours(8.0);

        Attendance day2 = new Attendance();
        day2.setDate(LocalDate.of(2024, 3, 2));
        day2.setStatus("LATE");
        day2.setTotalHours(7.0);

        when(attendanceRepository.findByEmployeeId(empId)).thenReturn(Arrays.asList(day1, day2));

        // When: طلب تقرير شهر 3 سنة 2024
        Map<String, Object> report = attendanceService.getEmployeePerformanceReport(empId, 3, 2024);

        // Then: التحقق من الحسابات (يومين حضور، واحد متأخر = 50% انضباط)
        assertThat(report.get("daysPresent")).isEqualTo(2L);
        assertThat(report.get("lateDays")).isEqualTo(1L);
        assertThat(report.get("punctualityRate")).isEqualTo("50%");
        assertThat(report.get("totalHours")).isEqualTo(15.0);
    }

    @Test
    @DisplayName("اختبار منع تسجيل الحضور مرتين في نفس اليوم")
    void checkIn_ShouldThrowException_IfAlreadyCheckedIn() {
        // Given
        Long empId = 1L;
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(new Employee()));
        when(attendanceRepository.findByEmployeeIdAndDate(eq(empId), any(LocalDate.class)))
            .thenReturn(Optional.of(new Attendance()));

        // When & Then
        assertThrows(RuntimeException.class, () -> attendanceService.checkIn(empId));
    }
}
