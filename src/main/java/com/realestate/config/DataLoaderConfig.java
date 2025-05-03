package com.realestate.config;

import com.realestate.util.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class DataLoaderConfig {

    @Autowired
    private DataLoader dataLoader;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        try {
            dataLoader.loadZoningData();
        } catch (Exception e) {
            System.err.println("Failed to load initial data: " + e.getMessage());
        }
    }
} 