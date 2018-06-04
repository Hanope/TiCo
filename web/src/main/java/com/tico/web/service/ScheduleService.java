package com.tico.web.service;

import static com.tico.web.model.ResponseStatus.*;
import com.tico.web.model.Day;
import com.tico.web.model.Hour;
import com.tico.web.model.ResponseMessage;
import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.timetable.schedule.ScheduleDTO;
import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.user.User;
import com.tico.web.repository.ScheduleRepository;
import com.tico.web.repository.TimetableRepository;
import com.tico.web.util.SessionUser;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private SessionUser sessionUser;

  public ResponseEntity<ResponseMessage> addSchedule(Long no, ScheduleDTO scheduleDTO) {
    ResponseMessage result;
    User user = sessionUser.getCurrentUser();
    Timetable timetable = timetableRepository.findOne(no);

    if (timetable == null) {
      result = new ResponseMessage(false, NOT_FOUND_TIMETABLE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    } else if (!isUsersTimetalbe(user, timetable.getUser())) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_OTHER_TIMETABLE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    List<Hour> hours = Hour.stringHoursToListHour(scheduleDTO.getHours());

    Schedule schedule = Schedule.builder()
        .name(scheduleDTO.getName())
        .day(scheduleDTO.getDay())
        .hours(hours)
        .build();

    return addSchedule(timetable, schedule);
  }

  private boolean isUsersTimetalbe(User sessionUser, User timetablesUser) {
    return timetablesUser.getNo().equals(sessionUser.getNo());
  }

  private ResponseEntity<ResponseMessage> addSchedule(Timetable timetable, Schedule newSchedule) {
    ResponseMessage result;
    List<Schedule> timetableSchedules = timetable.getSchedules();

    if (isExistSchedule(timetableSchedules, newSchedule)) {
      result = new ResponseMessage(false, EXISTS_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    } else {
      Schedule addedSchedule = addScheduleToRepository(newSchedule);
      addScheduleToTimetable(timetable, addedSchedule);
      result = new ResponseMessage(true, SUCCESS_ADD_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
//      result.put("result", true);
//      result.put("message", "일정이 추가되었습니다.");
//      result.put("data", addedSchedule);
    }
  }

  private void addScheduleToTimetable(Timetable timetable, Schedule schedule) {
    List<Schedule> schedules = timetable.getSchedules();
    schedules.add(schedule);
    timetable.setSchedules(schedules);
    timetableRepository.save(timetable);
  }

  private Schedule addScheduleToRepository(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }

  private boolean isExistSchedule(List<Schedule> timetableSchedules, Schedule newSchedule) {
    for (Schedule schedule : timetableSchedules) {
      if (isDuplicateDay(schedule.getDay(), newSchedule.getDay())) {
        if (isDuplicateHour(schedule.getHours(), newSchedule.getHours())) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isDuplicateDay(Day timetableDay, Day newScheduleDay) {
    return timetableDay.equals(newScheduleDay);
  }

  private boolean isDuplicateHour(List<Hour> timetableHours, List<Hour> newHours) {
    Collection<Hour> timetableHoursCollection = new HashSet<>(timetableHours);
    Collection<Hour> newHoursCollection = new HashSet<>(newHours);
    Collection<Hour> similar = new HashSet<>(timetableHoursCollection);
    similar.retainAll(newHoursCollection);

    return similar.size() > 0;
  }

}
