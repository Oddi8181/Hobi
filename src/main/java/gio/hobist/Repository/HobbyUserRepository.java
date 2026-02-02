package gio.hobist.Repository;

import gio.hobist.Entity.HobbyUser;
import gio.hobist.Entity.HobbyUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface HobbyUserRepository extends JpaRepository<HobbyUser, HobbyUserId> {


    void deleteByUserId(UUID userId);

    List<HobbyUser> findByUser_Id(UUID userId);

    void deleteByUser_Id(UUID userId);

    List<HobbyUser> findByHobby_IdIn(List<UUID> hobbyIds);
}
