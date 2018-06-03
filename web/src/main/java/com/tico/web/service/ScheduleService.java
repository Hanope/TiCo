package com.tico.web.service;

import com.tico.web.domain.Day;
import com.tico.web.domain.Hour;
import com.tico.web.domain.timetable.SyncTimetableDTO;
import com.tico.web.domain.timetable.schedule.Schedule;
import com.tico.web.domain.timetable.schedule.ScheduleDTO;
import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.user.User;
import com.tico.web.repository.ScheduleRepository;
import com.tico.web.repository.TimetableRepository;
import com.tico.web.util.SessionUser;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private SessionUser sessionUser;

  public Map<String, Object> addSchedule(Long no, ScheduleDTO scheduleDTO) {
    User user = sessionUser.getCurrentUser();
    Timetable timetable = timetableRepository.findOne(no);

    if (!isUsersTimetalbe(user, timetable.getUser())) {
      Map<String, Object> result = new HashMap<>();
      result.put("result", false);
      result.put("message", "본인의 시간표가 아니면 수정할 수 없습니다.");
      return result;
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

  private Map<String, Object> addSchedule(Timetable timetable, Schedule newSchedule) {
    Map<String, Object> result = new HashMap<>();
    List<Schedule> timetableSchedules = timetable.getSchedules();

    if (isExistSchedule(timetableSchedules, newSchedule)) {
      result.put("result", false);
      result.put("message", "이미 중복된 일정이 존재합니다.");
    } else {
      Schedule addedSchedule = addScheduleToRepository(newSchedule);
      result.put("result", true);
      result.put("message", "일정이 추가되었습니다.");
      addScheduleToTimetable(timetable, addedSchedule);
      result.put("data", addedSchedule);
    }

    return result;
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
