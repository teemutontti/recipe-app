package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Entity
@Data
@Table(name = "foods")
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    private String barcode;

    @Column(nullable = false)
    @Min(value = 1)
    private Integer servingSize = 100;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double calories;

    @Min(value = 0)
    private Double carbs;

    @Min(value = 0)
    private Double protein;

    @Min(value = 0)
    private Double fat;

    @Column(nullable = false)
    @NotNull
    private Integer createdBy;

    @Column(nullable = false)
    @NotNull
    private Integer editedBy;

    @CreationTimestamp
    @Column(nullable = false)
    private String created;

    @UpdateTimestamp
    @Column(nullable = false)
    private String edited;
}
