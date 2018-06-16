package com.tico.web.repository;

import com.tico.web.model.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findByNameOrId(String name, String id);

  boolean existsById(String id);

  User findOneById(String id);

  User findOneByToken(String token);
}
