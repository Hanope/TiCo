package com.tico.web.domain.project;

import com.tico.web.domain.user.User;
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
