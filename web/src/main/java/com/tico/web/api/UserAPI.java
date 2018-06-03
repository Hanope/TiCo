package com.tico.web.api;

import com.tico.web.service.UserService;
import com.tico.web.util.SessionUser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

  @Autowired
  private UserService userService;

  @Autowired
  private SessionUser sessionUser;

  @GetMapping("/{userName}")
  public Map<String, Object> getUser(@PathVariable String userName) {
    return userService.findOneByNameOrId(userName);
  }

  @PutMapping("/timetable/{no}")
  public Map<String, Object> updateRepresentTimetable(@PathVariable Long no) {
    return userService.updateRepresentTimetable(sessionUser, no);
  }

}
