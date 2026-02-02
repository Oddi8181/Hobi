package gio.hobist.Repository;

import gio.hobist.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByIdUser(UUID Id);

    Post findByid(UUID Id);

    List<Post> findAllByIdUserInOrderByCreatedAtDesc(List<UUID> ids);
}
