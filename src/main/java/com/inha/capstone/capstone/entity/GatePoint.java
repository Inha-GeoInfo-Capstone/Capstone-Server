package com.inha.capstone.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gate_points") // DB 테이블 (건물의 출입구 좌표들)
public class GatePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;


    public GatePoint() {}


    public GatePoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
