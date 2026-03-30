package com.company.erp_system.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.erp_system.entity.Attendance;
import com.company.erp_system.entity.Employee;
import com.company.erp_system.repository.AttendanceRepository;
import com.company.erp_system.repository.EmployeeRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

 

    public Attendance checkIn(Long employeeId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("الموظف غير موجود!"));

        Optional<Attendance> existingRecord = attendanceRepository.findByEmployeeIdAndDate(employeeId, today);
        if (existingRecord.isPresent()) {
			throw new RuntimeException("تم تسجيل الحضور مسبقاً!");
		}

        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setDate(today);
        attendance.setCheckIn(now);
        attendance.setStatus(now.isAfter(LocalTime.of(9, 0)) ? "LATE" : "PRESENT");

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("لا يوجد سجل حضور اليوم!"));

        if (attendance.getCheckOut() != null) {
			throw new RuntimeException("تم تسجيل الانصراف بالفعل!");
		}

        LocalTime now = LocalTime.now();
        attendance.setCheckOut(now);

        if (attendance.getCheckIn() != null) {
            long minutes = Duration.between(attendance.getCheckIn(), now).toMinutes();
            attendance.setTotalHours(Math.round((minutes / 60.0) * 100.0) / 100.0);
        }
        return attendanceRepository.save(attendance);
    }

  

    // 1. تقرير الموظفين الغائبين في تاريخ معين
    public List<Employee> getAbsenteesByDate(LocalDate date) {
        // جلب كل الموظفين
        List<Employee> allEmployees = employeeRepository.findAll();
        // جلب الموظفين الذين حضروا في هذا التاريخ
        List<Long> presentEmployeeIds = attendanceRepository.findByDate(date)
                .stream()
                .map(attendance -> attendance.getEmployee().getId())
                .collect(Collectors.toList());

        // تصفية: الموظف الغائب هو من ليس له ID في قائمة الحاضرين
        return allEmployees.stream()
                .filter(emp -> !presentEmployeeIds.contains(emp.getId()))
                .collect(Collectors.toList());
    }

    // 2. تقرير أداء الموظف التحليلي (شهري)
    public Map<String, Object> getEmployeePerformanceReport(Long employeeId, int month, int year) {
        List<Attendance> records = attendanceRepository.findByEmployeeId(employeeId).stream()
                .filter(a -> a.getDate().getMonthValue() == month && a.getDate().getYear() == year)
                .collect(Collectors.toList());

        long totalDaysPresent = records.size();
        long lateDays = records.stream().filter(a -> "LATE".equals(a.getStatus())).count();
        double totalWorkingHours = records.stream().mapToDouble(a -> a.getTotalHours() != null ? a.getTotalHours() : 0).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("employeeId", employeeId);
        report.put("period", month + "/" + year);
        report.put("daysPresent", totalDaysPresent);
        report.put("lateDays", lateDays);
        report.put("totalHours", totalWorkingHours);
        report.put("punctualityRate", totalDaysPresent == 0 ? 0 : ((totalDaysPresent - lateDays) * 100 / totalDaysPresent) + "%");

        return report;
    }

    public List<Attendance> getAllAttendance() { return attendanceRepository.findAll(); }
    public List<Attendance> getDailyReport(LocalDate date) { return attendanceRepository.findByDate(date); }
}
