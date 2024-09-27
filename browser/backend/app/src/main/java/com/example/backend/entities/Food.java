package com.example.backend.entities;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "foods")
public class Food implements BaseEntity<Food> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // NOT NULL
    private String barcode;
    private Integer servingSize;

    private Integer calories; // NOT NULL
    private Float carbs;
    private Float protein;
    private Float fat;

    private Integer createdBy;
    private Integer editedBy;

    @CreationTimestamp
    private String created;

    @UpdateTimestamp
    private String edited;

    @Override
    public boolean update(Food food) {
        try {
            if (food.getName() != null) { setName(food.getName()); }
            if (food.getCalories() != null) { setCalories(food.getCalories()); }
            if (food.getBarcode() != null) { setBarcode(food.getBarcode()); }
            if (food.getServingSize() != null) { setServingSize(food.getServingSize()); }
            if (food.getCarbs() != null) { setCarbs(food.getCarbs()); }
            if (food.getProtein() != null) { setProtein(food.getProtein()); }
            if (food.getFat() != null) { setFat(food.getFat()); }
            if (food.getCreatedBy() != null) { setCreatedBy(food.getCreatedBy()); }
            if (food.getEditedBy() != null) { setEditedBy(food.getEditedBy()); }
            if (food.getCreated() != null) { setCreated(food.getCreated()); }
            if (food.getEdited() != null) { setEdited(food.getEdited()); }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
