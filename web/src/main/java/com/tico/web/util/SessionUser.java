package com.tico.web.util;

import com.tico.web.model.user.User;
import com.tico.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionUser {

  @Autowired
  private UserRepository userRepository;

  public User getCurrentUser() {
    return userRepository.findOne(1L);
  }

  public boolean isSessionedUser(User sessionUser, User user) {
    return sessionUser.getNo().equals(user.getNo());
  }
}
