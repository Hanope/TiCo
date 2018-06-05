package com.tico.web.controller;

import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.user.User;
import com.tico.web.service.TimetableService;
import com.tico.web.util.SessionUser;
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
@RequestMapping("/timetable")
public class TimetableController {

  @Autowired
  private TimetableService timetableService;

  @Autowired
  private SessionUser sessionUser;

  @GetMapping("/create")
  public ModelAndView createTimetableView() {
    User user = sessionUser.getCurrentUser();
    return new ModelAndView("/timetable/timetable_create", "user", user);
  }

  @PostMapping("/create")
  public String createTimetable(@RequestParam(value = "timetable_name") String name) {
    User user = sessionUser.getCurrentUser();
    Timetable timetable = timetableService.createNewTimetable(name, user);

    return "redirect:" + "/timetable/" + timetable.getNo();
  }

  @GetMapping("/{no}")
  public ModelAndView show(@PathVariable Long no) {
    User user = sessionUser.getCurrentUser();
    return new ModelAndView("/timetable/timetable", "user", user);
  }

  @GetMapping("/sync")
  public ModelAndView syncView() {
    User user = sessionUser.getCurrentUser();
    return new ModelAndView("/timetable/sync", "user", user);
  }
}
