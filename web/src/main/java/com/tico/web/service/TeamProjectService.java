package com.tico.web.service;

import static com.tico.web.model.ResponseStatus.*;
import com.tico.web.model.Day;
import com.tico.web.model.Hour;
import com.tico.web.model.ResponseMessage;
import com.tico.web.model.project.TeamProject;
import com.tico.web.model.project.TeamProjectDTO;
import com.tico.web.model.project.TeamProjectVO;
import com.tico.web.model.project.TeamSchedule;
import com.tico.web.model.project.TeamScheduleDTO;
import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.timetable.TimetableDTO;
import com.tico.web.model.timetable.schedule.Schedule;
import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import com.tico.web.repository.LocationRepository;
import com.tico.web.repository.ScheduleRepository;
import com.tico.web.repository.TeamProjectRepository;
import com.tico.web.repository.TeamScheduleRepository;
import com.tico.web.repository.UserRepository;
import com.tico.web.util.SessionUser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  private LocationRepository locationRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private TeamScheduleRepository teamScheduleRepository;

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

  public ResponseEntity<ResponseMessage> getAllTeamProject(String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;
    List<TeamProjectVO> teamProjects = new ArrayList<>();

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    for (TeamProject teamProject : user.getTeamProjects()) {
      teamProjects.add(new TeamProjectVO(teamProject));
    }

    result = new ResponseMessage(true, teamProjects);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  public ResponseEntity<ResponseMessage> createNewProject(String projectName, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = TeamProjectDTO.builder()
        .name(projectName)
        .owner(user)
        .build()
        .toEntity();
    result = new ResponseMessage(true, teamProject);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  public ResponseEntity<ResponseMessage> getTeamMembers(Long projectNo, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

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

  public ResponseEntity<ResponseMessage> getTeamTimetableList(Long projectNo, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(projectNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<TimetableDTO> timetables = this.getTimetableList(teamProject);
    result = new ResponseMessage(true, timetables);

    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  public ResponseEntity<ResponseMessage> addMember(Long projectNo, String userId, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(projectNo);
    User owner = sessionUser.getUserByToken(token);
    User newUser = userRepository.findOneById(userId);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (!isOwner(teamProject.getOwner(), owner)) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_PROJECT_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    if (newUser == null) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (isExistsMember(teamProject, newUser)) {
      result = new ResponseMessage(false, EXISTS_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    teamProject.addMember(newUser);
    teamProjectRepository.save(teamProject);
    result = new ResponseMessage(true, newUser.getName() + "님이 추가되었습니다.");
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
  }

  private boolean isExistsMember(TeamProject teamProject, User user) {
    List<User> users = teamProject.getProjectMembers();
    return users.contains(user);
  }

  public ResponseEntity<ResponseMessage> deleteMember(Long projectNo, String userId, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(projectNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    User deleteUser = userRepository.findOneById(userId);

    if (!isOwner(teamProject.getOwner(), user)) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_PROJECT_MEMBER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    if (deleteUser == null || !isExistsMember(teamProject, deleteUser)) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (isOwner(teamProject.getOwner(), deleteUser)) {
      result = new ResponseMessage(false, CAN_NOT_DELETE_PROJECT_OWNER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    teamProject.deleteMember(deleteUser);
    teamProjectRepository.save(teamProject);
    result = new ResponseMessage(true, deleteUser.getName() + "님이 삭제되었습니다.");
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

  public ResponseEntity<ResponseMessage> getTeamSchedules(Long teamNo, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

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

  public ResponseEntity<ResponseMessage> getTeamScheduleByDate(Long teamNo, String date, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(teamNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();
    List<TeamSchedule> schedules = getTeamScheduleByDate(teamSchedules, date);

    if (schedules.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    result = new ResponseMessage(true, schedules);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  public ResponseEntity<ResponseMessage> getTeamScheduleByStartAndEndDate(Long teamNo, String startDate, String endDate, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(teamNo);

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<TeamSchedule> teamSchedules = teamProject.getTeamSchedule();
    List<TeamSchedule> schedules = getTeamScheduleByStartAndEndDate(teamSchedules, startDate, endDate);

    if (schedules.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    result = new ResponseMessage(true, schedules);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }

  private List<TeamSchedule> getTeamScheduleByDate(List<TeamSchedule> teamSchedules, String date) {
    List<TeamSchedule> teamScheduleList = new ArrayList<>();

//    for (TeamSchedule teamSchedule : teamSchedules) {
//      if (teamSchedule.getCompareDate().equals(date))
//        teamScheduleList.add(teamSchedule);
//    }

    try {
      Date cDate = new SimpleDateFormat("yyyyMMdd").parse(date);
      for (TeamSchedule teamSchedule : teamSchedules) {
        Date d = teamSchedule.getRealDate();
        if (cDate.compareTo(d) == 0)
          teamScheduleList.add(teamSchedule);
      }
    } catch (ParseException e) { }
    return teamScheduleList;
  }

  private List<TeamSchedule> getTeamScheduleByStartAndEndDate(List<TeamSchedule> teamSchedules, String startDate, String endDate) {
    List<TeamSchedule> teamScheduleList = new ArrayList<>();

    try {
      Date startD = new SimpleDateFormat("yyyyMMdd").parse(startDate);
      Date endD = new SimpleDateFormat("yyyyMMdd").parse(endDate);
      for (TeamSchedule teamSchedule : teamSchedules) {
        Date date = teamSchedule.getRealDate();
        if (date.compareTo(startD) == 0 || date.compareTo(endD) == 0 || startD.compareTo(date) * date.compareTo(endD) > 0)
          teamScheduleList.add(teamSchedule);
      }
    } catch (ParseException e) { }

    return teamScheduleList;
  }

  public ResponseEntity<ResponseMessage> getAllTeamSchedules(Long teamNo, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

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
  public ResponseEntity<ResponseMessage> addTeamSchedule(Long teamNo, TeamScheduleDTO teamScheduleDTO, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;
    List<TeamSchedule> teamSchedules;
    String date = teamScheduleDTO.getDate();
    boolean isScheduled = true;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    TeamProject teamProject = teamProjectRepository.findOne(teamNo);
    teamSchedules = teamProject.getTeamSchedule();
    TeamSchedule newTeamSchedule = teamScheduleDTO.toEntity();

    if (teamProject == null) {
      result = new ResponseMessage(false, NOT_FOUND_PROJECT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<Schedule> scheduleList = new ArrayList<>();

    // 해당 요일에 시간표 비교 하는거

    for (TeamSchedule teamSchedule : getTeamScheduleByDate(teamSchedules, date)) {
      scheduleList.add(teamSchedule.getSchedule());
    }

    for (TimetableDTO timetableDTO : getTimetableList(teamProject)) {
      for (Schedule schedule : timetableDTO.getSchedules()) {
        scheduleList.add(schedule);
      }
    }
    isScheduled = isExistSchedule(scheduleList, newTeamSchedule.getSchedule());


    if (isScheduled) {
      result = new ResponseMessage(false, EXISTS_SCHEDULE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
    }


    locationRepository.save(newTeamSchedule.getLocation());
    scheduleRepository.save(newTeamSchedule.getSchedule());
    teamScheduleRepository.save(newTeamSchedule);

    teamProject.addSchedule(newTeamSchedule);


    teamProjectRepository.save(teamProject);

    result = new ResponseMessage(true, SUCCESS_ADD_SCHEDULE);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.CREATED);
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
