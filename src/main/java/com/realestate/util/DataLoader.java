package com.realestate.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Component
public class DataLoader {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void loadZoningData() {
        // Check if data exists
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM real_estate_zoning",
            Integer.class
        );

        if (count != null && count > 0) {
            System.out.println("Data already exists in the database. Count: " + count);
            return;
        }

        System.out.println("No data found in the database. Please ensure data is migrated properly.");
    }

    public void clearExistingData() {
        jdbcTemplate.execute("DELETE FROM real_estate_zoning");
    }
} 