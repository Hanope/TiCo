package com.tico.web.model.user;

import com.tico.web.util.SHA256;
import lombok.Setter;

@Setter
public class UserJoinDTO {

  private String id;
  private String name;
  private String password;
  private String studentNo;

  public User toEntity() {
    return User.builder()
        .id(this.id)
        .password(this.password)
        .name(this.name)
        .studentNo(this.studentNo)
        .role("USER")
        .token(SHA256.generateSHA256(id))
        .build();
  }
}
