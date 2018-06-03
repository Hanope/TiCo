package com.tico.web.domain.user;

import lombok.Getter;

@Getter
public class UserDTO {

  private String id;
  private String name;
  private String studentNo;

  public UserDTO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.studentNo = user.getStudentNo();
  }
}
