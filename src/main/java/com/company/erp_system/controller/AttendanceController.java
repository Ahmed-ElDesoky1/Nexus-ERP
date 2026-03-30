package com.company.erp_system.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // استيراد ضروري
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.erp_system.entity.Attendance;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // 1. تسجيل الحضور: مسموح للموظف والمدير (كلٌ حسب هويته)
    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> checkIn(@RequestParam Long employeeId) {
        try {
            Attendance attendance = attendanceService.checkIn(employeeId);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. تسجيل الانصراف: مسموح للموظف والمدير
    @PostMapping("/check-out")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> checkOut(@RequestParam Long employeeId) {
        try {
            Attendance attendance = attendanceService.checkOut(employeeId);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. عرض جميع السجلات: للمدير فقط (ADMIN)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    // 4. التقرير اليومي العام: للمدير فقط
    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Attendance>> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getDailyReport(date));
    }

    // 5. تقرير الموظفين الغائبين: للمدير فقط
    @GetMapping("/absentees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAbsentees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAbsenteesByDate(date));
    }

    // 6. تقرير أداء الموظف: مسموح للموظف رؤية تقريره وللمدير رؤية أي تقرير
    @GetMapping("/performance/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or #employeeId == authentication.principal.id")
    public ResponseEntity<Map<String, Object>> getEmployeePerformance(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(attendanceService.getEmployeePerformanceReport(employeeId, month, year));
    }
}
