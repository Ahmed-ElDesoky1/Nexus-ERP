package com.company.erp_system.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getWelcomeMessage() {
        return "Welcome to ERP System!";
    }

    public String getSystemStatus() {
        return "ERP System is running successfully!";
    }
}
