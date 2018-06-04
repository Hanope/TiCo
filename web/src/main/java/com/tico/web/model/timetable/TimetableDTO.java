package com.tico.web.model.timetable;

import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.user.UserDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimetableDTO {
    private Long no;

    private String name;

    private List<Schedule> schedules;

    private UserDTO user;
}
