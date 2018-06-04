package com.tico.web.model.project;

import com.tico.web.model.user.User;
import lombok.Builder;

@Builder
public class TeamProjectDTO {

  private String name;
  private User owner;

  public TeamProject toEntity() {
    TeamProject teamProject = new TeamProject();
    teamProject.setName(name);
    teamProject.setOwner(owner);
    teamProject.addMember(owner);
    return teamProject;
  }
}
