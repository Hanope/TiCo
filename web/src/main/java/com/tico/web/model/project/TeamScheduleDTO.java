package com.tico.web.model.project;

import com.tico.web.model.Hour;
import com.tico.web.model.Location;
import com.tico.web.model.LocationDTO;
import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.timetable.schedule.ScheduleDTO;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamScheduleDTO {

  private Date date;

  private LocationDTO location;

//  private List<ScheduleDTO> schedules;

  private ScheduleDTO schedule;

  private String title;

  private String content;

  public TeamSchedule toEntity() {
    TeamSchedule teamSchedule = new TeamSchedule();
//    List<Schedule> scheduleList = new ArrayList<>();

//    for (ScheduleDTO scheduleDTO : schedules) {
//      Schedule schedule = Schedule.builder()
//          .name(scheduleDTO.getName())
//          .day(scheduleDTO.getDay())
//          .hours(Hour.stringHoursToListHour(scheduleDTO.getHours()))
//          .build();
//
//      scheduleList.add(schedule);
//    }

    Schedule newSchedule = Schedule.builder()
        .name(schedule.getName())
        .day(schedule.getDay())
        .hours(Hour.stringHoursToListHour(schedule.getHours()))
        .build();

//    scheduleList.add(schedule);

    Location location = new Location();
    location.setName(this.location.getName());
    location.setAddress(this.location.getAddress());

    teamSchedule.setTitle(title);
    teamSchedule.setContent(content);
    teamSchedule.setLocation(location);
//    teamSchedule.setSchedules(scheduleList);
    teamSchedule.setSchedule(newSchedule);
    teamSchedule.setDate(date);

    return teamSchedule;
  }

  public String getDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return sdf.format(this.date);
  }

}
