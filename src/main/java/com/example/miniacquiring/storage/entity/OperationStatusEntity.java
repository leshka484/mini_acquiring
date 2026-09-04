package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_status", schema = "operation")
public class OperationStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status; // Статус
}
