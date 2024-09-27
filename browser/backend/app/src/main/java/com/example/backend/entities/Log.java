package com.example.backend.entities;
import java.sql.Date;
import java.sql.Time;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "logs")
public class Log implements BaseEntity<Log> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date; // NOT NULL
    private Time time;
    private String meal; // NOT NULL
    private Long user;
    private Long food;

    @Override
    public boolean update(Log log) {
        try {
            if (log.getDate() != null) { setDate(log.getDate()); }
            if (log.getTime() != null) { setTime(log.getTime()); }
            if (log.getMeal() != null) { setMeal(log.getMeal()); }
            if (log.getUser() != null) { setUser(log.getUser()); }
            if (log.getFood() != null) { setFood(log.getFood()); }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
