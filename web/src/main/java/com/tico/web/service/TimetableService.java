package com.tico.web.service;

import static com.tico.web.model.ResponseStatus.INVALID_TOKEN;

import com.tico.web.model.ResponseMessage;
import com.tico.web.model.Hour;
import com.tico.web.model.ResponseStatus;
import com.tico.web.model.timetable.SyncTimetableDTO;
import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.timetable.schedule.ScheduleDTO;
import com.tico.web.model.user.User;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tico.web.repository.*;
import org.springframework.transaction.annotation.Transactional;

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
  public ResponseEntity<ResponseMessage> syncTimetable(SyncTimetableDTO timetable) {
    ResponseMessage result;
    User user = sessionUser.getCurrentUser();

    Timetable newTimetable = Timetable.builder()
        .name(timetable.getName())
        .user(user)
        .build();

    List<Schedule> schedules = addSchedule(timetable.getSchedules());
    newTimetable.setSchedules(schedules);
    newTimetable.setUser(user);

    result = new ResponseMessage(true, timetableRepository.save(newTimetable));
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
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

  public ResponseEntity<ResponseMessage> findOne(Long no, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    Timetable timetable = timetableRepository.findOne(no);

    if (timetable == null) {
      result = new ResponseMessage(false, ResponseStatus.NOT_FOUND_TIMETABLE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    result = new ResponseMessage(true, timetable);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

}
