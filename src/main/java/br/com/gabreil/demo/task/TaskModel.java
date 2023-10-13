package br.com.gabreil.demo.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity( name = "tb_task")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    @Column(length = 50)
    private String tittle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTittle(String tittle) throws Exception {
        if (tittle.length() > 50) {
            throw new Exception("The title must contain a maximum of 50 characters");
        }

        this.tittle = tittle;
    }

}
