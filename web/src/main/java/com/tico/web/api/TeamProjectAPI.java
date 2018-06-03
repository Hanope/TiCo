package com.tico.web.api;

import com.tico.web.service.TeamProjectService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team/{teamNo}")
public class TeamProjectAPI {

  @Autowired
  private TeamProjectService teamProjectService;

  @GetMapping("/timetable")
  public Map<String, Object> getTimetables(@PathVariable Long teamNo) {
    return teamProjectService.getTeamTimetableList(teamNo);
  }

  @GetMapping("/schedule/{date}")
  public Map<String, Object> getScheduleByDate(@PathVariable Long teamNo, @PathVariable String date) {
    return teamProjectService.getTeamScheduleByDate(teamNo, date);
  }

  @GetMapping("/schedule")
  public Map<String, Object> getSchedules(@PathVariable Long teamNo) {
    return teamProjectService.getTeamSchedules(teamNo);
  }

  @PostMapping("/{userId}")
  public Map<String, Object> addMember(@PathVariable Long teamNo, @PathVariable String userId) {
    return teamProjectService.addMember(teamNo, userId);
  }

  @DeleteMapping("/{userId}")
  public Map<String, Object> deleteMember(@PathVariable Long teamNo, @PathVariable String userId) {
    return teamProjectService.deleteMember(teamNo, userId);
  }
}
