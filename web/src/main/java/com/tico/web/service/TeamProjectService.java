package com.tico.web.service;

import com.tico.web.domain.project.TeamProject;
import com.tico.web.domain.project.TeamProjectDTO;
import com.tico.web.domain.project.TeamSchedule;
import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.timetable.TimetableDTO;
import com.tico.web.domain.user.User;
import com.tico.web.domain.user.UserDTO;
import com.tico.web.repository.TeamProjectRepository;
import com.tico.web.repository.UserRepository;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamProjectService {

  @Autowired
  private SessionUser sessionUser;

  @Autowired
  private TeamProjectRepository teamProjectRepository;

  @Autowired
  private UserRepository userRepository;

  public TeamProject createNewProject(String projectName) {
    User user = sessionUser.getCurrentUser();
    TeamProject teamProject = TeamProjectDTO.builder()
        .name(projectName)
        .owner(user)
        .build()
        .toEntity();
    return teamProjectRepository.save(teamProject);
  }

  public Map<String, Object> getTeamTimetableList(Long projectNo) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);

    if (teamProject == null) {
      result.put("result", false);
      result.put("message", "존재하지 않는 프르젝트입니다.");
    }

    List<TimetableDTO> timetables = this.getTimetableList(teamProject);

    result.put("result", true);
    result.put("message", timetables);
    return result;
  }

  public Map<String, Object> addMember(Long projectNo, String userId) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);
    User owner = sessionUser.getCurrentUser();
    User user = userRepository.findOneById(userId);

    if (!isOwner(teamProject.getOwner(), owner)) {
      result.put("result", false);
      result.put("message", "프로젝트 개설자만 추가할 수 있습니다.");
      return result;
    }

    teamProject.addMember(user);
    teamProjectRepository.save(teamProject);
    result.put("result", true);
    result.put("message", user.getName() + "님이 추가되었습니다.");
    return result;
  }

  public Map<String,Object> deleteMember(Long projectNo, String userId) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);
    User owner = sessionUser.getCurrentUser();
    User user = userRepository.findOneById(userId);

    if (teamProject == null) {
      result.put("result", false);
      result.put("message", "Null pointer Exception");
      return result;
    }

    if (isOwner(teamProject.getOwner(), user)) {
      result.put("result", false);
      result.put("message", "프로젝트 개설자는 삭제할 수 없습니다.");
      return result;
    }

    if (!isOwner(teamProject.getOwner(), owner)) {
      result.put("result", false);
      result.put("message", "프로젝트 개설자만 삭제할 수 있습니다.");
      return result;
    }

    teamProject.deleteMember(user);
    teamProjectRepository.save(teamProject);
    result.put("result", true);
    result.put("message", user.getName() + "님이 삭제되었습니다.");

    return result;
  }

  private boolean isOwner(User projectOwner, User sessionUser) {
    if (projectOwner.getId().equals(sessionUser.getId()))
      return true;
    else
      return false;
  }

  private List<TimetableDTO> getTimetableList(TeamProject teamProject) {
    List<User> projectMembers = teamProject.getProjectMembers();
    List<TimetableDTO> timetables = new ArrayList<>();

    for (User member : projectMembers) {
      TimetableDTO dto = createTimetable(member);
      if (dto == null)
        continue;
      timetables.add(dto);
    }

    return timetables;
  }

  private TimetableDTO createTimetable(User member) {
    TimetableDTO dto = new TimetableDTO();
    Timetable timetable = member.getTimetable();

    if (timetable == null)
      return null;

    dto.setNo(timetable.getNo());
    dto.setName(timetable.getName());
    dto.setUser(new UserDTO(member));
    dto.setSchedules(timetable.getSchedules());

    return dto;
  }

  public Map<String,Object> getSchedules(Long teamNo) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();

    if (teamSchedules.size() == 0) {
      result.put("result", false);
      result.put("message", "팀 일정이 존재하지 않습니다.");
      return result;
    }

    result.put("result", true);
    result.put("message", teamSchedules);
    return result;
  }

  public Map<String,Object> getTeamSchedules(Long teamNo) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = getSchedulesAfterToday(teamProject.getTeamSchedule());

    if (teamSchedules.size() == 0) {
      result.put("result", false);
      result.put("message", "해당 날짜에 일정이 존재하지 않습니다.");
      return result;
    }

    result.put("result", true);
    result.put("message", teamSchedules);
    return result;
  }

  private List<TeamSchedule> getSchedulesAfterToday(List<TeamSchedule> teamSchedule) {
    List<TeamSchedule> teamSchedules = new ArrayList<>();
    Date date = new Date();

    for (TeamSchedule schedule : teamSchedule) {
      if (schedule.getRealDate().after(date))
        teamSchedules.add(schedule);
    }

    return teamSchedules;
  }

  public Map<String,Object> getTeamScheduleByDate(Long teamNo, String date) {
    Map<String, Object> result = new HashMap<>();
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();
    List<TeamSchedule> schedules = getTeamScheduleByDate(teamSchedules, date);

    if (schedules.size() == 0) {
      result.put("result", false);
      result.put("message", "해당 날짜에 일정이 존재하지 않습니다.");
      return result;
    }

    result.put("result", true);
    result.put("message", schedules);
    return result;
  }

  private List<TeamSchedule> getTeamScheduleByDate(List<TeamSchedule> teamSchedules, String date) {
    List<TeamSchedule> teamScheduleList = new ArrayList<>();

    for (TeamSchedule teamSchedule : teamSchedules) {
      if (teamSchedule.getCompareDate().equals(date))
        teamScheduleList.add(teamSchedule);
    }

    return teamScheduleList;
  }
}
