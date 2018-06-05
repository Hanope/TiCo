package com.tico.web.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class LoginUserDetails extends User {

  private long no;

  public LoginUserDetails(com.tico.web.model.user.User user) {
    super(user.getId(), user.getPassword(), AuthorityUtils.createAuthorityList("USER"));
    no = user.getNo();
  }

  public long getNo() {
    return no;
  }
}
