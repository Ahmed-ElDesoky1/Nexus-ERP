package com.company.erp_system.entity;

/**
 * Enum يمثل حالات المهام في نظام الـ ERP.
 * تم استخدامه في كلاس Task لتنظيم سير العمل.
 */
public enum TaskStatus {

    TODO,          // المهمة في قائمة الانتظار (لم تبدأ)

    IN_PROGRESS,   // الموظف بدأ العمل فعلياً على المهمة

    COMPLETED,     // تم الانتهاء من المهمة وإغلاقها

    CANCELLED      // تم إلغاء المهمة لأي سبب إداري
}
