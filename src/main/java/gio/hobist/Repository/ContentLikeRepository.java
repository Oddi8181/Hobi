package gio.hobist.Repository;

import gio.hobist.Entity.ContentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ContentLikeRepository extends JpaRepository<ContentLike, UUID> {

    List<ContentLike> findByPostId(UUID postId);

    List<ContentLike> findByCommentId(UUID commentId);

    List<ContentLike> findByUserId(UUID userId);

    void deleteByPostIdAndUserIdAndCommentId(UUID postId, UUID userId, UUID commentId);

    void deleteByPostId(UUID postId);

    List<ContentLike> findByPostIdAndUserIdAndCommentId(UUID postId, UUID userId, UUID commentId);
}
