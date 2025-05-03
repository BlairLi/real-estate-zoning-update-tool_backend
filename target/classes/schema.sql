-- Enable PostGIS extension if not already enabled
CREATE EXTENSION IF NOT EXISTS postgis;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS zoning_audit_log;
DROP TABLE IF EXISTS real_estate_zoning;

-- Create zoning table
CREATE TABLE real_estate_zoning (
    id SERIAL PRIMARY KEY,
    geom geometry(Polygon,4326),
    name VARCHAR(80),
    ll_uuid VARCHAR(80),
    mail_city VARCHAR(80),
    mail_zip VARCHAR(80),
    mailadd VARCHAR(80),
    ogc_fid INTEGER,
    owner VARCHAR(80),
    parcelnumb VARCHAR(80),
    path VARCHAR(80),
    struct INTEGER,
    structstyl VARCHAR(80),
    usedesc VARCHAR(80),
    zoning VARCHAR(80),
    zoning_sub VARCHAR(80),
    zoning_typ VARCHAR(80)
);

-- Create audit log table with proper constraints
CREATE TABLE zoning_audit_log (
    id SERIAL PRIMARY KEY,
    action_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    affected_parcels INTEGER NOT NULL,
    new_zoning_type VARCHAR(80) NOT NULL,
    details TEXT
);

-- Create indexes for better query performance
CREATE INDEX idx_audit_log_timestamp ON zoning_audit_log(action_timestamp);
CREATE INDEX idx_audit_log_action_type ON zoning_audit_log(action_type); 