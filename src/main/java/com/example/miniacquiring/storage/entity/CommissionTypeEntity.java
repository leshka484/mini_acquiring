package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "commission_type", schema = "commission")
public class CommissionTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String type;
}
