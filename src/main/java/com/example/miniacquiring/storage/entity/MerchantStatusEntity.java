package com.example.miniacquiring.storage.entity;

import com.example.miniacquiring.core.Const;
import com.example.miniacquiring.core.enums.MerchantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchant_status", schema = Const.STATUS_SCHEMA)
public class MerchantStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true)
    private MerchantStatus code;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
