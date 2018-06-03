package com.tico.web.domain.timetable.schedule;

import com.tico.web.domain.Day;
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
