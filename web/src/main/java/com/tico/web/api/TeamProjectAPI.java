package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.model.project.TeamScheduleDTO;
import com.tico.web.service.TeamProjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team/{teamNo}")
public class TeamProjectAPI {

  @Autowired
  private TeamProjectService teamProjectService;

  @GetMapping("/timetable")
  public ResponseEntity<ResponseMessage> getTimetables(@PathVariable Long teamNo) {
    return teamProjectService.getTeamTimetableList(teamNo);
  }

  // 해당 날짜의 회의 일정
  @GetMapping("/schedule/{date}")
  public ResponseEntity<ResponseMessage> getScheduleByDate(@PathVariable Long teamNo, @PathVariable String date) {
    return teamProjectService.getTeamScheduleByDate(teamNo, date);
  }

  @PostMapping("/schedule")
  public ResponseEntity<ResponseMessage> addTeamSchedule(@PathVariable Long teamNo, @RequestBody TeamScheduleDTO schedule) {
    return teamProjectService.addTeamSchedule(teamNo, schedule);
  }

  // 오늘 이후 유효한 회의 일정
  @GetMapping("/schedule")
  public ResponseEntity<ResponseMessage> getTeamSchedules(@PathVariable Long teamNo) {
    return teamProjectService.getTeamSchedules(teamNo);
  }

  // 모든 회의 일정
  @GetMapping("/schedule/all")
  public ResponseEntity<ResponseMessage> getAllTeamSchedules(@PathVariable Long teamNo) {
    return teamProjectService.getAllTeamSchedules(teamNo);
  }

  @GetMapping("/members")
  public ResponseEntity<ResponseMessage> getTeamMembers(@PathVariable Long teamNo) {
    return teamProjectService.getTeamMembers(teamNo);
  }

  @PostMapping("/members/{userId}")
  public ResponseEntity<ResponseMessage> addMember(@PathVariable Long teamNo, @PathVariable String userId) {
    return teamProjectService.addMember(teamNo, userId);
  }

  @DeleteMapping("/members/{userId}")
  public ResponseEntity<ResponseMessage> deleteMember(@PathVariable Long teamNo, @PathVariable String userId) {
    return teamProjectService.deleteMember(teamNo, userId);
  }
}
