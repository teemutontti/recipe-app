package com.example.backend.entities;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
public class Log implements BaseEntity<Log> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date; // NOT NULL
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time time;
    private String meal; // NOT NULL
    private Integer user;
    private Integer food;
    private Integer amount;

    @Override
    public boolean update(Log log) {
        try {
            if (log.getDate() != null) { setDate(log.getDate()); }
            if (log.getTime() != null) { setTime(log.getTime()); }
            if (log.getMeal() != null) { setMeal(log.getMeal()); }
            if (log.getUser() != null) { setUser(log.getUser()); }
            if (log.getFood() != null) { setFood(log.getFood()); }
            if (log.getAmount() != null) { setAmount(log.getAmount()); }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
