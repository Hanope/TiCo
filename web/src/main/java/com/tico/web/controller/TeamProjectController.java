package com.tico.web.controller;

import com.tico.web.model.project.TeamProject;
import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import com.tico.web.repository.TeamProjectRepository;
import com.tico.web.service.NavigationService;
import com.tico.web.service.TeamProjectService;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/team")
public class TeamProjectController {

  @Autowired
  private SessionUser sessionUser;

  @Autowired
  private TeamProjectRepository teamProjectRepository;

  @Autowired
  private NavigationService navigationService;

  @Autowired
  private TeamProjectService teamProjectService;

  @GetMapping("")
  public ModelAndView viewCreateTeam() {
    ModelAndView modelAndView = navigationService.getNavigationUserInfo();
    modelAndView.setViewName("/team/team_create");
    return modelAndView;
  }

  @GetMapping("/{no}")
  public ModelAndView viewProjectByNo(@PathVariable Long no) {
    ModelAndView modelAndView = navigationService.getNavigationUserInfo();
    TeamProject teamProject = teamProjectRepository.findOne(no);

    if (teamProject == null) {
      return new ModelAndView("redirect:/404");
    }

    List<User> teamMember = teamProject.getProjectMembers();
    List<UserDTO> teamMemberDTO = new ArrayList<>();

    for (User user : teamMember) {
      UserDTO userDTO = new UserDTO(user);
      teamMemberDTO.add(userDTO);
    }

    modelAndView.addObject("teamMember", teamMemberDTO);
    modelAndView.setViewName("/team/team");
    return modelAndView;
  }

  @PostMapping("")
  public ModelAndView createNewProject(@RequestParam(value = "project_name") String projectName) {
    ModelAndView modelAndView = navigationService.getNavigationUserInfo();
    TeamProject teamProject = teamProjectService.createNewProject(projectName);

    modelAndView.setViewName("redirect:/team/" + teamProject.getNo());
    return modelAndView;
  }

  @GetMapping("/meeting/{no}")
  public ModelAndView viewMeeting(@PathVariable Long no) {
    ModelAndView modelAndView = navigationService.getNavigationUserInfo();
    TeamProject teamProject = teamProjectRepository.findOne(no);

    if (teamProject == null) {
      return new ModelAndView("redirect:/404");
    }

    modelAndView.setViewName("/team/meeting");
    return modelAndView;
  }

}
