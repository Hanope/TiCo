package com.tico.web.domain.timetable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tico.web.domain.DayHours;
import com.tico.web.domain.timetable.schedule.Schedule;
import com.tico.web.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Timetable {

  @Id
  @GeneratedValue
  private Long no;

  @Column(length = 30, nullable = false)
  private String name;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "timetable_schedule")
  @Setter
  private List<Schedule> schedules;

  @ManyToOne
  @JsonIgnore
  @Setter
  private User user;

  @Builder
  public Timetable(String name, User user, List<Schedule> schedules) {
    this.name = name;
    this.user = user;
    this.schedules = schedules;
  }

  public List<DayHours> getScheduleDayHour() {
    List<DayHours> dayHours = new ArrayList<>();

    for (Schedule schedule : schedules) {
      DayHours dayHour = new DayHours();
      dayHour.setDay(schedule.getDay());
      dayHour.setHours(schedule.getHours());
      dayHours.add(dayHour);
    }
    return dayHours;
  }
}
