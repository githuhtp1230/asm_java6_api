package com.example.asm_java6_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Nationalized
    @Lob
    @Column(name = "name")
    private String name;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Nationalized
    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Nationalized
    @Column(name = "brand")
    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("NULL")
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ColumnDefault("getdate()")
    @Column(name = "created_date")
    private Instant createdDate;

    @ColumnDefault("getdate()")
    @Column(name = "updated_date")
    private Instant updatedDate;

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "product")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductImage> productImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductReview> productReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductSpecification> productSpecifications = new LinkedHashSet<>();

}