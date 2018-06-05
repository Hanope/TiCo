package com.tico.web.controller;

import com.tico.web.model.project.TeamProject;
import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import com.tico.web.repository.TeamProjectRepository;
import com.tico.web.service.TeamProjectService;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  private TeamProjectService teamProjectService;

  @GetMapping()
  public ModelAndView viewCreateTeam(Model model) {
    User user = sessionUser.getCurrentUser();
    return new ModelAndView("/team/team_create", "user", user);
  }

  @GetMapping("/{no}")
  public ModelAndView viewProjectByNo(@PathVariable Long no) {
    ModelAndView modelAndView = new ModelAndView();
    User user = sessionUser.getCurrentUser();
    TeamProject teamProject = teamProjectRepository.findOne(no);
    modelAndView.addObject("user", user);

    if (teamProject == null) {
      return new ModelAndView("redirect:/404", "user", user);
    }

    List<User> teamMember = teamProject.getProjectMembers();
    List<UserDTO> teamMemberDTO = new ArrayList<>();

    for (User u : teamMember) {
      UserDTO userDTO = new UserDTO(u);
      teamMemberDTO.add(userDTO);
    }

    modelAndView.addObject("teamMember", teamMemberDTO);
    modelAndView.setViewName("/team/team");
    return modelAndView;
  }

  @PostMapping("")
  public ModelAndView createNewProject(@RequestParam(value = "project_name") String projectName) {
    User user = sessionUser.getCurrentUser();
    TeamProject teamProject = teamProjectService.createNewProject(projectName);

    return new ModelAndView("redirect:/team/" + teamProject.getNo(), "user", user);
  }

  @GetMapping("/meeting/{no}")
  public ModelAndView viewMeeting(@PathVariable Long no) {
    User user = sessionUser.getCurrentUser();
    TeamProject teamProject = teamProjectRepository.findOne(no);

    if (teamProject == null) {
      return new ModelAndView("redirect:/404", "user", user);
    }

    return new ModelAndView("/team/meeting", "user", user);
  }

}
