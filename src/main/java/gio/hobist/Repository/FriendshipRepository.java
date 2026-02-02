package gio.hobist.Repository;

import gio.hobist.Dto.FriendshipDto;
import gio.hobist.Entity.Friendship;
import gio.hobist.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {


    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.user1.id = :user1 AND f.user2.id = :user2) OR " +
            "(f.user1.id = :user2 AND f.user2.id = :user1)")
    Friendship findByUser1IdAndUser2Id(UUID user1, UUID user2);

    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.user1.id = :user1) OR " +
            "(f.user2.id = :user1)")
    List<Friendship> findByUser1Id(UUID user1);

    @Query("SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
            "FROM Friendship f " +
            "WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = :status")
    List<UUID> findFriendIds(UUID userId, Status status);
}
