package com.tico.web.controller;

import com.tico.web.model.user.User;
import com.tico.web.util.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class HomeController {

  @Autowired
  private SessionUser sessionUser;

  @GetMapping("/")
  public ModelAndView index() {
    User user = sessionUser.getCurrentUser();
    return new ModelAndView("index", "user", user);
  }

}
