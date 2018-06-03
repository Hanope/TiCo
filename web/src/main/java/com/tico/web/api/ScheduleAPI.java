package com.tico.web.api;

import com.tico.web.domain.timetable.schedule.ScheduleDTO;
import com.tico.web.service.ScheduleService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleAPI {

  @Autowired
  private ScheduleService scheduleService;

  @PostMapping("/{no}")
  public Map<String, Object> addSchedule(@PathVariable Long no, @RequestBody ScheduleDTO scheduleDTO) {
    return scheduleService.addSchedule(no, scheduleDTO);
  }
}
