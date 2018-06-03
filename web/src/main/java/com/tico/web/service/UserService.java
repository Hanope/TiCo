package com.tico.web.service;

import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.user.User;
import com.tico.web.domain.user.UserDTO;
import com.tico.web.domain.user.UserJoinDTO;
import com.tico.web.repository.TimetableRepository;
import com.tico.web.repository.UserRepository;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TimetableRepository timetableRepository;

  public User findOne(Long no) {
    return userRepository.findOne(no);
  }

  public Map<String, Object> findOneByNameOrId(String name) {
    Map<String, Object> result = new HashMap<>();

    result.put("result", false);
    if (name == null) {
      result.put("message", "null을 입력할 수 없습니다.");
      return result;
    }

    List<User> userList = userRepository.findByNameOrId(name, name);
    if (userList.size() == 0) {
      result.put("message", "존재하지 않는 회원입니다.");
      return result;
    }

    result.put("result", true);
    List<UserDTO> userDTOList = new ArrayList<>();
    for (User user : userList) {
      UserDTO userDTO = new UserDTO(user);
      userDTOList.add(userDTO);
    }
    result.put("message", userDTOList);

    return result;
  }

  public User addNewTimetable(User user, Timetable timetable) {
    user.addTimetable(timetable);
    return userRepository.save(user);
  }

  public boolean join(UserJoinDTO userJoinDTO) {
    User user = userJoinDTO.toEntity();

    if (userRepository.existsById(user.getId()))
      return false;

    userRepository.save(user);
    return true;
  }

  public Map<String,Object> updateRepresentTimetable(SessionUser sessionUser, Long no) {
    Map<String, Object> result = new HashMap<>();
    Timetable timetable = timetableRepository.findOne(no);
    User user = sessionUser.getCurrentUser();

    if (user.getTimetable().getUser().getId() != timetable.getUser().getId()) {
      result.put("result", false);
      result.put("message", "다른 사람의 시간표를 수정할 수 없습니다.");
      return result;
    }

    user.setTimetable(timetable);
    userRepository.save(user);

    result.put("result", true);
    result.put("message", "대표 시간표 설정이 되었습니다.");
    return result;
  }
}