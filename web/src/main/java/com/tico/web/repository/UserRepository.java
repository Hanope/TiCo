package com.tico.web.repository;

import com.tico.web.model.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  public List<User> findByNameOrId(String name, String id);

  public boolean existsById(String id);

  public User findOneById(String id);
}
