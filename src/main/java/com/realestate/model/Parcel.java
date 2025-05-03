package com.realestate.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Polygon;

@Entity
@Table(name = "real_estate_zoning")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "geom", columnDefinition = "geometry(Polygon,4326)")
    private Polygon geometry;

    @Column(name = "zoning_typ")
    private String zoningType;

    // Additional fields from the database
    private String name;
    
    @Column(name = "ll_uuid")
    private String llUuid;
    
    @Column(name = "mail_city")
    private String mailCity;
    
    @Column(name = "mail_zip")
    private String mailZip;
    
    @Column(name = "mailadd")
    private String mailAdd;
    
    @Column(name = "ogc_fid")
    private Integer ogcFid;
    
    private String owner;
    
    @Column(name = "parcelnumb")
    private String parcelNumber;
    
    private String path;
    
    private Integer struct;
    
    @Column(name = "structstyl")
    private String structStyle;
    
    @Column(name = "usedesc")
    private String useDesc;
    
    private String zoning;
    
    @Column(name = "zoning_sub")
    private String zoningSub;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Polygon getGeometry() {
        return geometry;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }

    public String getZoningType() {
        return zoningType;
    }

    public void setZoningType(String zoningType) {
        this.zoningType = zoningType;
    }

    // Additional getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLlUuid() {
        return llUuid;
    }

    public void setLlUuid(String llUuid) {
        this.llUuid = llUuid;
    }

    public String getMailCity() {
        return mailCity;
    }

    public void setMailCity(String mailCity) {
        this.mailCity = mailCity;
    }

    public String getMailZip() {
        return mailZip;
    }

    public void setMailZip(String mailZip) {
        this.mailZip = mailZip;
    }

    public String getMailAdd() {
        return mailAdd;
    }

    public void setMailAdd(String mailAdd) {
        this.mailAdd = mailAdd;
    }

    public Integer getOgcFid() {
        return ogcFid;
    }

    public void setOgcFid(Integer ogcFid) {
        this.ogcFid = ogcFid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStruct() {
        return struct;
    }

    public void setStruct(Integer struct) {
        this.struct = struct;
    }

    public String getStructStyle() {
        return structStyle;
    }

    public void setStructStyle(String structStyle) {
        this.structStyle = structStyle;
    }

    public String getUseDesc() {
        return useDesc;
    }

    public void setUseDesc(String useDesc) {
        this.useDesc = useDesc;
    }

    public String getZoning() {
        return zoning;
    }

    public void setZoning(String zoning) {
        this.zoning = zoning;
    }

    public String getZoningSub() {
        return zoningSub;
    }

    public void setZoningSub(String zoningSub) {
        this.zoningSub = zoningSub;
    }
} 