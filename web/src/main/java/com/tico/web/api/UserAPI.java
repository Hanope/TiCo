package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

  @Autowired
  private UserService userService;

  @GetMapping("/{userName}")
  public ResponseEntity<ResponseMessage> getUser(@PathVariable String userName) {
    return userService.findOneByNameOrId(userName);
  }

  @PutMapping("/timetable/{no}")
  public ResponseEntity<ResponseMessage> updateRepresentTimetable(@PathVariable Long no, @RequestHeader(value = "TiCo-Token") String token) {
    return userService.updateRepresentTimetable(no, token);
  }

}
