package gio.hobist.Repository;

import gio.hobist.Entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HobbyRepository extends JpaRepository<Hobby, UUID> {

//    List<Hobby> findById(List<UUID> hobbyIds);
}
