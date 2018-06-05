package com.tico.web.util;

import com.tico.web.model.user.User;
import com.tico.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SessionUser {

  @Autowired
  private UserRepository userRepository;

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    return userRepository.findOneById(userName);
  }

  public User getUserByToken(String token) {
    return userRepository.findOneByToken(token);
  }
}
