package gio.hobist.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "id_user")
    private UUID idUser;

    private String message;

    @Column(name = "image")
    private String image;

    @Column(name = "like_number")
    private Integer likeNumber; //M.G: this is unesecery and will be removed in future

    @Column(name="created_at",insertable = false,nullable = false)
    private Timestamp createdAt;

}
