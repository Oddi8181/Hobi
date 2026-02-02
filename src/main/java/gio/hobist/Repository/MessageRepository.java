package gio.hobist.Repository;

import gio.hobist.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByFriendshipId(UUID friendshipId);

//    @Query("SELECT f FROM Message m")
    List<Message> findTop36ByFriendshipIdOrderByTimeSentDesc(UUID friendshipId);

    List<Message> findByUserId(UUID userId);

}
