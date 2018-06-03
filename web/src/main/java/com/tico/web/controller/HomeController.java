package com.tico.web.controller;

import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.user.User;
import com.tico.web.service.TimetableService;
import com.tico.web.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @Autowired
  private UserService userService;

  @Autowired
  private TimetableService timetableService;

  @GetMapping("/")
  public ModelAndView index() {
    // TODO: 실제 세션을 받아와서 해줘야 함
    User user = userService.findOne(1L);
    List<Timetable> timetableList = timetableService.userTimetableList(user);

    return new ModelAndView("index", "timetables", timetableList);
  }
}
