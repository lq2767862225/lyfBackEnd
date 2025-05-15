package site.rainbowx.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import site.rainbowx.backend.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> { // 修正主键类型为Long
    Optional<User> findByEmail(String email);
}