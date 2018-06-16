package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.model.timetable.schedule.ScheduleDTO;
import com.tico.web.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleAPI {

  @Autowired
  private ScheduleService scheduleService;

  @PostMapping("/{timetableNo}")
  public ResponseEntity<ResponseMessage> addSchedule(@PathVariable Long timetableNo, @RequestBody ScheduleDTO scheduleDTO, @RequestHeader(value="TiCo-Token") String token) {
    return scheduleService.addSchedule(timetableNo, scheduleDTO, token);
  }

  @DeleteMapping("/{timetableNo}/{scheduleNo}")
  public ResponseEntity<ResponseMessage> deleteSchedule(@PathVariable Long timetableNo, @PathVariable Long scheduleNo, @RequestHeader(value="TiCo-Token") String token) {
    return scheduleService.deleteSchedule(timetableNo, scheduleNo, token);
  }

}
