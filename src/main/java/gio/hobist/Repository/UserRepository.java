package gio.hobist.Repository;

import gio.hobist.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.lang.Integer;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    User findByid(UUID Id);

    List<User> findByCountryId(Integer countryId);

    List<User> findByCityId(Integer cityId);

    List<User> findByNameAndSurname(String name, String surname);

    List<User> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String name, String surname);
  
    boolean existsByEmail(String newEmail);
}
