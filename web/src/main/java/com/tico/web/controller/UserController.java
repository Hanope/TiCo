package com.tico.web.controller;

import com.tico.web.domain.user.UserJoinDTO;
import com.tico.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/join")
  public String viewJoinPage() {
    return "/user/signup";
  }

  @PostMapping("/join")
  public String join(UserJoinDTO userDTO) {
    if (userService.join(userDTO)) {
      return "redirect:/user/login";
    }
    return "redirect:/error/500";
  }

  @GetMapping("/login")
  public String viewLoginPage() {
    return "/user/login";
  }

}
