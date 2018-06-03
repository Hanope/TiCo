package com.tico.web.domain.project;

import com.tico.web.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TeamProject {

  @Id
  @GeneratedValue
  private Long no;

  private String name;

  @OneToMany
  private List<TeamSchedule> teamSchedule;

  @JoinTable(
      name = "TEAM_PROJECT_USER",
      joinColumns = @JoinColumn(
          name = "TEAM_PROJECT_NO",
          referencedColumnName = "NO"
      ),
      inverseJoinColumns = @JoinColumn(
          name = "USER_NO",
          referencedColumnName = "NO"
      )
  )
  @ManyToMany
  private List<User> projectMembers = new ArrayList<>();

  @OneToOne
  private User owner;

  public void addMember(User user) {
    projectMembers.add(user);
  }

  public void deleteMember(User user) {
    projectMembers.remove(user);
  }
}
