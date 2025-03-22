package com.example.asm_java6_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "description")
    private String description;

    @Nationalized
    @Lob
    @Column(name = "image")
    private String image;

    @ColumnDefault("1")
    @Column(name = "active")
    private Boolean active;

    @ColumnDefault("getdate()")
    @Column(name = "created_date")
    private Instant createdDate;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new LinkedHashSet<>();

}