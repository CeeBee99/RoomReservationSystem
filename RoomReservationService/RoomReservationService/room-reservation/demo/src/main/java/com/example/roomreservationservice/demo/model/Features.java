package com.example.roomreservationservice.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Features {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean elevatorAccess;
    private boolean whiteboard;
    private boolean projector;
    private boolean computerAccess;
    private boolean largeTable;
    private boolean quietRoom;

    @OneToOne
    @JoinColumn(name = "room_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Rooms room;
}