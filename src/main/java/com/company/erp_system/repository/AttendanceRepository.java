package com.company.erp_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.erp_system.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 1. للبحث عن جميع سجلات حضور موظف معين (مفيد لعرض كشف حساب حضور للموظف)
    List<Attendance> findByEmployeeId(Long employeeId);

    // 2. للبحث عن سجل موظف في تاريخ محدد (يستخدم في Check-in و Check-out لمنع التكرار)
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    // 3.  للبحث عن جميع السجلات في تاريخ معين (لإنشاء التقارير اليومية)
    List<Attendance> findByDate(LocalDate date);
}
