package com.tico.web.service;

import com.tico.web.domain.user.User;
import com.tico.web.util.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class NavigationService {

  @Autowired
  private SessionUser sessionUser;

  public ModelAndView getNavigationUserInfo() {
    ModelAndView modelAndView = new ModelAndView();
    User user = sessionUser.getCurrentUser();

    modelAndView.addObject("timetables", user.getTimetables());
    modelAndView.addObject("projects", user.getTeamProjects());

    return modelAndView;
  }
}
