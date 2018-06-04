package com.tico.web.model.timetable;

import com.tico.web.model.timetable.schedule.ScheduleDTO;
import com.tico.web.model.user.UserDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncTimetableDTO {

  private Long no;

  private String name;

  private List<ScheduleDTO> schedules;

  private UserDTO user;

}
