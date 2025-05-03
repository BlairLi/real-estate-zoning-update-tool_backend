package com.realestate.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class DataLoader {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void loadZoningData() {
        // Check if data already exists
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM real_estate_zoning",
            Integer.class
        );

        if (count != null && count > 0) {
            System.out.println("Data already exists in the database. Skipping data load.");
            return;
        }

        System.out.println("No data found. Loading from remote database...");
        
        // Load data from remote database
        String sql = "INSERT INTO real_estate_zoning (" +
            "geom, name, ll_uuid, mail_city, mail_zip, mailadd, " +
            "ogc_fid, owner, parcelnumb, path, struct, structstyl, " +
            "usedesc, zoning, zoning_sub, zoning_typ" +
            ") " +
            "SELECT " +
            "geom, name, ll_uuid, mail_city, mail_zip, mailadd, " +
            "ogc_fid, owner, parcelnumb, path, struct, structstyl, " +
            "usedesc, zoning, zoning_sub, zoning_typ " +
            "FROM dblink('postgres://real_estate:ZT9b0qv6iQ@108.61.159.122:13432/postgres', " +
            "'SELECT geom, name, ll_uuid, mail_city, mail_zip, mailadd, " +
            "ogc_fid, owner, parcelnumb, path, struct, structstyl, " +
            "usedesc, zoning, zoning_sub, zoning_typ " +
            "FROM real_estate_zoning') " +
            "AS remote_data (" +
            "geom geometry(Polygon,4326), " +
            "name VARCHAR(80), " +
            "ll_uuid VARCHAR(80), " +
            "mail_city VARCHAR(80), " +
            "mail_zip VARCHAR(80), " +
            "mailadd VARCHAR(80), " +
            "ogc_fid INTEGER, " +
            "owner VARCHAR(80), " +
            "parcelnumb VARCHAR(80), " +
            "path VARCHAR(80), " +
            "struct INTEGER, " +
            "structstyl VARCHAR(80), " +
            "usedesc VARCHAR(80), " +
            "zoning VARCHAR(80), " +
            "zoning_sub VARCHAR(80), " +
            "zoning_typ VARCHAR(80)" +
            ")";

        try {
            jdbcTemplate.execute(sql);
            System.out.println("Successfully loaded data from remote database.");
        } catch (Exception e) {
            System.err.println("Error loading data from remote database: " + e.getMessage());
            throw new RuntimeException("Failed to load data from remote database", e);
        }
    }

    public void clearExistingData() {
        jdbcTemplate.execute("DELETE FROM real_estate_zoning");
    }
} 