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

    public RoadLink() {}

    public RoadLink(RoadCenter from, RoadCenter to) {
        this.from = from;
        this.to = to;
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
}