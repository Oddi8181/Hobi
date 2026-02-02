package gio.hobist.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
    private UUID idSender;
    private UUID idReceiver;
    private String subject;
    private String description;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Notification(UUID idSender, UUID idReceiver, String subject, String description) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.subject = subject;
        this.description = description;
        this.createdAt=null;
    }
    

}