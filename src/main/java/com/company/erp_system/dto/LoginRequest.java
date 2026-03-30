package com.company.erp_system.dto;

public class LoginRequest {
    private String username;
    private String password;

    // --- أضف هذه الدوال يدوياً الآن لإنهاء الخطأ ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
