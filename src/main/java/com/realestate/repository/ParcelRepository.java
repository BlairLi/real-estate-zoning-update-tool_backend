package com.realestate.repository;

import com.realestate.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    @Query(value = "SELECT id, ST_AsGeoJSON(geom) as geometry, zoning_typ, name, owner, mailadd, mail_city, mail_zip, parcelnumb, usedesc FROM real_estate_zoning", nativeQuery = true)
    List<Object[]> findAllParcelsWithGeoJSON();
} 