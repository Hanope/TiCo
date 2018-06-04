package com.tico.web.model.timetable.schedule;

import com.tico.web.model.Day;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDTO {

  private String name;
  private Day day;
  private List<String> hours;

}
