package com.tico.web.model.user;

import com.tico.web.model.project.TeamProject;
import com.tico.web.model.timetable.Timetable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long no;

  @Column(length = 20, nullable = false)
  private String id;

  @Column(nullable = false)
  private String password;

  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 10, nullable = false)
  private String studentNo;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_timetable")
  private List<Timetable> timetables;

  @JoinTable(
      name = "TEAM_PROJECT_USER",
      joinColumns = @JoinColumn(
          name = "USER_NO",
          referencedColumnName = "NO"
      ),
      inverseJoinColumns = @JoinColumn(
          name = "TEAM_PROJECT_NO",
          referencedColumnName = "NO"
      )
  )
  @ManyToMany
  private List<TeamProject> teamProjects;

  @OneToOne
  private Timetable timetable;

  @Builder
  public User(String id, String password, String name, String studentNo) {
    this.id = id;
    this.password = password;
    this.name = name;
    this.studentNo = studentNo;
  }

  public void addTimetable(Timetable timetable) {
    this.timetables.add(timetable);
  }

  public void setTimetable(Timetable timetable) {
    this.timetable = timetable;
  }

}
