package com.tico.web.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tico.web.model.Location;
import com.tico.web.model.timetable.schedule.Schedule;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamSchedule {

  @Id
  @GeneratedValue
  private Long no;

  private Date date;

  @OneToOne
  @JoinTable(name = "team_schedule_location")
  private Location location;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "team_timetable_schedule")
  private List<Schedule> schedules;

  private String title;

  private String content;

  @JsonIgnore
  public Date getRealDate() {
    return date;
  }

  public String getDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
    return sdf.format(date);
  }

  @JsonIgnore
  public String getCompareDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
    return sdf.format(date);
  }
}
