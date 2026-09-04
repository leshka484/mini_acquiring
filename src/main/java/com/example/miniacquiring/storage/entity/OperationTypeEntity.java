package com.example.miniacquiring.storage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation_type", schema = "operation")
public class OperationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
}
