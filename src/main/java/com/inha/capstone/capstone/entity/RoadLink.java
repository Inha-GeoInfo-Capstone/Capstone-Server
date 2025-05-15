package com.inha.capstone.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "road_links")
public class RoadLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_center_id", nullable = false)
    private RoadCenter from;

    @ManyToOne
    @JoinColumn(name = "to_center_id", nullable = false)
    private RoadCenter to;

    @Column(nullable = false)
    private double distance;

    public RoadLink() {}

    public RoadLink(RoadCenter from, RoadCenter to) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public RoadCenter getFrom() {
        return from;
    }

    public RoadCenter getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }

    public void setFrom(RoadCenter from) {
        this.from = from;
    }

    public void setTo(RoadCenter to) {
        this.to = to;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}