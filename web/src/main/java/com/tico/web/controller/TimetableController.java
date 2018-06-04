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
  public String createTimetableView() {
    return "/timetable/timetable_create";
  }

  @PostMapping("/create")
  public String createTimetable(@RequestParam(value = "timetable_name") String name) {
    User user = sessionUser.getCurrentUser();
    Timetable timetable = timetableService.createNewTimetable(name, user);

    return "redirect:" + "/timetable/" + timetable.getNo();
  }

  @GetMapping("/{no}")
  public String show(@PathVariable Long no) {
    return "/timetable/timetable";
  }

  @GetMapping("/sync")
  public String syncView() {
    return "/timetable/sync";
  }
}
