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
@Table(name = "content_like")
public class ContentLike {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_post", nullable =true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "id_comment",nullable = true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public ContentLike() { super(); }
}
