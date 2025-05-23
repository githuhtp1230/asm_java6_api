package com.example.asm_java6_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 255)
    @Nationalized
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "birthday")
    private Instant birthday;

    @Size(max = 13)
    @Column(name = "phone", length = 13)
    private String phone;

    @Size(max = 255)
    @Nationalized
    @Column(name = "address")
    private String address;

    @Size(max = 50)
    @ColumnDefault("'User'")
    @Column(name = "role", length = 50)
    private String role = "User";

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "user")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ProductReview> productReviews = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }
}