package gio.hobist.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private String message;

    @Column(name = "like_number")
    private Integer likeNumber;

    public Comment(Comment c){
        this.id=c.id;
        this.post=c.post;
        this.user=c.user;
        this.message=c.message;
        this.likeNumber=c.likeNumber;
    }
}


