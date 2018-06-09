package com.tico.web.model.project;

import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamProjectVO {

  private Long no;

  private String name;

  private List<UserDTO> projectMembers;

  private UserDTO owner;

  public TeamProjectVO(TeamProject teamProject) {
    this.no = teamProject.getNo();
    this.name = teamProject.getName();
    this.owner = new UserDTO(teamProject.getOwner());
    projectMembers = new ArrayList<>();
    for (User u : teamProject.getProjectMembers())
      projectMembers.add(new UserDTO(u));
  }
}
