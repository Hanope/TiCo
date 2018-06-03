package com.tico.web.domain.user;

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
        .build();
  }

}
