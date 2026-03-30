# 🏢 نظام إدارة المؤسسة الذكي (Enterprise Smart Management System) 🚀

نظام **ERP** متكامل تم تطويره باستخدام **Java Spring Boot 3**، يهدف إلى أتمتة العمليات الإدارية في المؤسسات، بدءاً من إدارة الموظفين والأقسام وصولاً إلى تتبع المشاريع والمهام والتقارير التحليلية، مع نظام أمني صارم وتوثيق شامل.

---

## ✨ المميزات الرئيسية (Core Features)

### 🔐 الأمان والهوية (Security & Auth)
*   **JWT Authentication:** نظام تسجيل دخول آمن باستخدام توكن (JSON Web Tokens).
*   **Role-Based Access Control (RBAC):** توزيع صلاحيات دقيق (ADMIN, MANAGER, EMPLOYEE).
*   **Security Refinement:** حماية المسارات (Endpoints) على مستوى الميثود باستخدام `@PreAuthorize`.

### 📊 الوحدات الإدارية (Modules)
*   **إدارة المشاريع (Project Module):** متابعة دورة حياة المشاريع وحالتها (IN_PROGRESS, COMPLETED).
*   **إدارة المهام (Task Management):** توزيع المهام ومتابعة حالة الإنجاز (TODO, DONE).
*   **الحضور والانصراف (Attendance System):** تسجيل أوقات الدخول والخروج (Check-in/Check-out).
*   **التقارير والتحليلات (Reports & Analytics):** إصدار تقارير أداء الموظفين وحالة المشاريع.

---

## 🛠️ التقنيات المستخدمة (Tech Stack)

*   **Backend:** Java 21 / Spring Boot 3.x
*   **Database:** MySQL / Hibernate JPA
*   **Security:** Spring Security & JWT
*   **Build Tool:** Maven
*   **API Testing:** **Postman** & **Swagger UI**

---

## 🚀 التشغيل والاختبار (Setup & Testing)

### 1. المتطلبات الأساسية
*   Java JDK 21
*   MySQL Database (اسم القاعدة: `erp_system_db`)

### 2. واجهة التوثيق (Swagger UI)
بمجرد تشغيل المشروع، يمكنك الوصول للتوثيق التفاعلي واختبار الـ APIs عبر:
`http://localhost:8081/swagger-ui/index.html`

### 3. أدوات الاختبار الخارجية (Postman)
تم اختبار جميع المسارات (Endpoints) يدوياً باستخدام **Postman** لضمان استقرار النظام ومعالجة الـ Headers والـ Tokens بشكل صحيح.

---

## 🔐 دليل الاختبار السريع (E2E Scenario)
للتأكد من عمل النظام، اتبع الخطوات التالية:
1.  **Signup:** أنشئ حساباً جديداً في `/api/auth/signup`.
2.  **Signin:** سجل دخولك للحصول على الـ `accessToken`.
3.  **Authorize:** في Swagger، اضغط **Authorize** والصق التوكن (بصيغة `Bearer <TOKEN>`).
4.  **Execute:** ابدأ بإضافة الأقسام والمشاريع والمهام بنجاح!

---

## 📂 هيكل المجلدات (Project Structure)
*   `controller`: نقاط الوصول للـ API.
*   `service`: المنطق البرمجي للأعمال.
*   `repository`: التفاعل مع قاعدة البيانات.
*   `security`: إعدادات الحماية وتشفير التوكن.
*   `dto`: كائنات نقل البيانات (Data Transfer Objects).

---
> **ملاحظة:** هذا المشروع هو نتاج 30 يوماً من التطوير المكثف، يغطي كافة جوانب الـ Enterprise Applications.
