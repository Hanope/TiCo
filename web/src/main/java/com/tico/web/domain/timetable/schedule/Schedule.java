package com.tico.web.domain.timetable.schedule;

import com.tico.web.domain.Day;
import com.tico.web.domain.Hour;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Schedule {

  @Id
  @GeneratedValue
  private Long no;

  @Column(length = 30, nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  private Day day;

  @ManyToMany(fetch = FetchType.LAZY)
//  @JoinTable(name = "schedule_hour")
  @JoinTable(name = "schedule_hour",
            joinColumns = @JoinColumn(name = "schedule_no"),
            inverseJoinColumns = @JoinColumn(name = "hour_code"))
  private List<Hour> hours;

  @Builder
  public Schedule(String name, Day day, List<Hour> hours) {
    this.name = name;
    this.day = day;
    this.hours = hours;
  }

}
