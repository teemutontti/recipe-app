package com.example.backend.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
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
    private String name;

    private String barcode;

    @Column(nullable = false)
    private Integer servingSize = 100;

    @Column(nullable = false)
    private Double calories;

    private Double carbs;
    private Double protein;
    private Double fat;

    @Column(nullable = false)
    private Integer createdBy;

    @Column(nullable = false)
    private Integer editedBy;

    @CreationTimestamp
    @Column(nullable = false)
    private String created;

    @UpdateTimestamp
    @Column(nullable = false)
    private String edited;
}
