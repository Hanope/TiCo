package com.tico.web.service;

import static com.tico.web.model.ResponseStatus.*;
import com.tico.web.model.Day;
import com.tico.web.model.Hour;
import com.tico.web.model.ResponseMessage;
import com.tico.web.model.project.TeamProject;
import com.tico.web.model.project.TeamProjectDTO;
import com.tico.web.model.project.TeamSchedule;
import com.tico.web.model.project.TeamScheduleDTO;
import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.timetable.TimetableDTO;
import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import com.tico.web.repository.TeamProjectRepository;
import com.tico.web.repository.UserRepository;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public ResponseEntity<ResponseMessage> getTeamMembers(Long projectNo) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<UserDTO> members = this.getTeamMembers(teamProject);
    result = new ResponseMessage(true, members);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  private List<UserDTO> getTeamMembers(TeamProject teamProject) {
    List<UserDTO> userList = new ArrayList<>();

    for (User user : teamProject.getProjectMembers()) {
      userList.add(new UserDTO(user));
    }

    return userList;
  }

  public ResponseEntity<ResponseMessage> getTeamTimetableList(Long projectNo) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<TimetableDTO> timetables = this.getTimetableList(teamProject);
    result = new ResponseMessage(true, timetables);

    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  public ResponseEntity<ResponseMessage> addMember(Long projectNo, String userId) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);
    User owner = sessionUser.getCurrentUser();
    User user = userRepository.findOneById(userId);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (!isOwner(teamProject.getOwner(), owner)) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_PROJECT_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    if (user == null) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (isExistsMember(teamProject, user)) {
      result = new ResponseMessage(false, EXISTS_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    teamProject.addMember(user);
    teamProjectRepository.save(teamProject);
    result = new ResponseMessage(true, user.getName() + "님이 추가되었습니다.");
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
  }

  private boolean isExistsMember(TeamProject teamProject, User user) {
    List<User> users = teamProject.getProjectMembers();
    return users.contains(user);
  }

  public ResponseEntity<ResponseMessage> deleteMember(Long projectNo, String userId) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(projectNo);
    User owner = sessionUser.getCurrentUser();
    User user = userRepository.findOneById(userId);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (user == null || !isExistsMember(teamProject, user)) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (isOwner(teamProject.getOwner(), user)) {
      result = new ResponseMessage(false, CAN_NOT_DELETE_PROJECT_OWNER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    if (!isOwner(teamProject.getOwner(), owner)) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_PROJECT_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    teamProject.deleteMember(user);
    teamProjectRepository.save(teamProject);
    result = new ResponseMessage(true, user.getName() + "님이 삭제되었습니다.");
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
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

  public ResponseEntity<ResponseMessage> getTeamSchedules(Long teamNo) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = getSchedulesAfterToday(teamProject.getTeamSchedule());

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (teamSchedules.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
    }

    result = new ResponseMessage(true, teamSchedules);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
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

  public ResponseEntity<ResponseMessage> getTeamScheduleByDate(Long teamNo, String date) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();
    List<TeamSchedule> schedules = getTeamScheduleByDate(teamSchedules, date);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (schedules.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    result = new ResponseMessage(true, schedules);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  private List<TeamSchedule> getTeamScheduleByDate(List<TeamSchedule> teamSchedules, String date) {
    List<TeamSchedule> teamScheduleList = new ArrayList<>();

    for (TeamSchedule teamSchedule : teamSchedules) {
      if (teamSchedule.getCompareDate().equals(date))
        teamScheduleList.add(teamSchedule);
    }

    return teamScheduleList;
  }

  public ResponseEntity<ResponseMessage> getAllTeamSchedules(Long teamNo) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (teamSchedules.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    result = new ResponseMessage(true, teamSchedules);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  // TODO 프로젝트 시간표를 가져와야하는 동시에 개인 시간표도 모두 가져와야 함, 그다음 시간이 겹치는지 확인하고 겹치면 추가 X 날짜는 어떻게,,?
  @Transactional
  public ResponseEntity<ResponseMessage> addTeamSchedule(Long teamNo, TeamScheduleDTO teamScheduleDTO) {
    ResponseMessage result;
    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    TeamSchedule teamSchedule = teamScheduleDTO.toEntity();

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (isExistSchedule(teamProject.getTeamSchedule(), teamSchedule.getSchedules().get(0))) {
      result = new ResponseMessage(false, EXISTS_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    teamProject.addSchedule(teamSchedule);
    teamProjectRepository.save(teamProject);

    result = new ResponseMessage(false, SUCCESS_ADD_SCHEDULE);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
  }

  private boolean isExistSchedule(List<TeamSchedule> teamSchedules, Schedule newSchedule) {
    for (TeamSchedule teamSchedule : teamSchedules) {
      for (Schedule schedule : teamSchedule.getSchedules()) {
        if (isDuplicateDay(schedule.getDay(), newSchedule.getDay())) {
          if (isDuplicateHour(schedule.getHours(), newSchedule.getHours())) {
            return true;
          }
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
