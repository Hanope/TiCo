package com.tico.web.domain.timetable.schedule;

import com.tico.web.domain.Day;
import com.tico.web.domain.Hour;
import java.util.List;
import lombok.Getter;

@Getter
public class ScheduleResponseDTO {

  private Long no;
  private String name;
  private Day day;
  private List<Hour> hours;

  public ScheduleResponseDTO(Schedule entity) {
    this.no = entity.getNo();
    this.name = entity.getName();
    this.day = entity.getDay();
    this.hours = entity.getHours();
  }
}
