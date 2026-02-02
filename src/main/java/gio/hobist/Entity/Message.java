package gio.hobist.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_friendship")
    private Friendship friendship;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private String message;

    @Column(name = "file_name")
    private String file_name;

    @Column(name = "file")
    private String file;

    @Column(name = "time_sent",insertable = false,nullable = false)
    private Timestamp timeSent;

}
