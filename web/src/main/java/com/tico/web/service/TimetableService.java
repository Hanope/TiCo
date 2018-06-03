package com.tico.web.service;

import com.tico.web.domain.Hour;
import com.tico.web.domain.timetable.SyncTimetableDTO;
import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.timetable.schedule.Schedule;
import com.tico.web.domain.timetable.schedule.ScheduleDTO;
import com.tico.web.domain.user.User;
import com.tico.web.util.SessionUser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tico.web.repository.*;
import org.springframework.transaction.annotation.Transactional;
import sun.net.www.http.HttpClient;

@Service
public class TimetableService {

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private SessionUser sessionUser;

  @Transactional
  public Timetable createNewTimetable(String name, User user) {
    Timetable timetable = Timetable.builder()
        .name(name)
        .user(user)
        .build();

    timetable = timetableRepository.save(timetable);
    userService.addNewTimetable(user, timetable);

    return timetable;
  }

  @Transactional
  public Map<String, Object> syncTimetable(SyncTimetableDTO timetable) {
    Map<String, Object> result = new HashMap<>();
    User user = sessionUser.getCurrentUser();

    Timetable newTimetable = Timetable.builder()
        .name(timetable.getName())
        .user(user)
        .build();

    List<Schedule> schedules = addSchedule(timetable.getSchedules());
    newTimetable.setSchedules(schedules);
    newTimetable.setUser(user);

    result.put("result", true);
    result.put("message", timetableRepository.save(newTimetable));

    return result;
  }

  private List<Schedule> addSchedule(List<ScheduleDTO> schedulesDTO) {
    List<Schedule> schedules = new ArrayList<>();

    for (ScheduleDTO scheduleDTO : schedulesDTO) {
      List<Hour> hours = Hour.stringHoursToListHour(scheduleDTO.getHours());
      Schedule schedule = Schedule.builder()
          .name(scheduleDTO.getName())
          .day(scheduleDTO.getDay())
          .hours(hours)
          .build();

      Schedule addedSchedule = addScheduleToRepository(schedule);
      schedules.add(addedSchedule);
    }

    return schedules;
  }

  private Schedule addScheduleToRepository(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }

  public List<Timetable> userTimetableList(User user) {
    List<Timetable> timetables = user.getTimetables();
    return timetables;
  }

  public Timetable findOne(Long no) {
    return timetableRepository.findOne(no);
  }

}
