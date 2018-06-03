package com.tico.web.domain.timetable;

import com.tico.web.domain.timetable.schedule.Schedule;
import com.tico.web.domain.user.UserDTO;
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
