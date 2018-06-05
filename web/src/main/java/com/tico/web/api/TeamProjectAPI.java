package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.model.project.TeamProject;
import com.tico.web.model.project.TeamScheduleDTO;
import com.tico.web.service.TeamProjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
public class TeamProjectAPI {

  @Autowired
  private TeamProjectService teamProjectService;

  @PostMapping()
  public ResponseEntity<ResponseMessage> createNewProject(@RequestBody Map<String, String> name, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.createNewProject(token, name.get("name"));
  }

  @GetMapping("/{teamNo}/timetable")
  public ResponseEntity<ResponseMessage> getTimetables(@PathVariable Long teamNo, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.getTeamTimetableList(teamNo, token);
  }

  // 해당 날짜의 회의 일정
  @GetMapping("/{teamNo}/schedule/{date}")
  public ResponseEntity<ResponseMessage> getScheduleByDate(@PathVariable Long teamNo, @PathVariable String date, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.getTeamScheduleByDate(teamNo, date, token);
  }

  @PostMapping("/{teamNo}/schedule")
  public ResponseEntity<ResponseMessage> addTeamSchedule(@PathVariable Long teamNo, @RequestBody TeamScheduleDTO schedule, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.addTeamSchedule(teamNo, schedule, token);
  }

  // 오늘 이후 유효한 회의 일정
  @GetMapping("/{teamNo}/schedule")
  public ResponseEntity<ResponseMessage> getTeamSchedules(@PathVariable Long teamNo, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.getTeamSchedules(teamNo, token);
  }

  // 모든 회의 일정
  @GetMapping("/{teamNo}/schedule/all")
  public ResponseEntity<ResponseMessage> getAllTeamSchedules(@PathVariable Long teamNo, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.getAllTeamSchedules(teamNo, token);
  }

  @GetMapping("/{teamNo}/members")
  public ResponseEntity<ResponseMessage> getTeamMembers(@PathVariable Long teamNo, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.getTeamMembers(teamNo, token);
  }

  @PostMapping("/{teamNo}/members/{userId}")
  public ResponseEntity<ResponseMessage> addMember(@PathVariable Long teamNo, @PathVariable String userId, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.addMember(teamNo, userId, token);
  }

  @DeleteMapping("/{teamNo}/members/{userId}")
  public ResponseEntity<ResponseMessage> deleteMember(@PathVariable Long teamNo, @PathVariable String userId, @RequestHeader(value="TiCo-Token") String token) {
    return teamProjectService.deleteMember(teamNo, userId, token);
  }
}
