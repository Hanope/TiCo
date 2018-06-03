package com.tico.web.domain.timetable;

import com.tico.web.domain.timetable.schedule.ScheduleDTO;
import com.tico.web.domain.user.UserDTO;
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
