package com.tico.web.model.timetable.schedule;

import com.tico.web.model.Day;
import com.tico.web.model.Hour;
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
