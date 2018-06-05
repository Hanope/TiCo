package com.tico.web.service;

import static com.tico.web.model.ResponseStatus.*;
import com.tico.web.model.ResponseMessage;
import com.tico.web.model.timetable.Timetable;
import com.tico.web.model.user.User;
import com.tico.web.model.user.UserDTO;
import com.tico.web.model.user.UserJoinDTO;
import com.tico.web.repository.TimetableRepository;
import com.tico.web.repository.UserRepository;
import com.tico.web.util.SessionUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private SessionUser sessionUser;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TimetableRepository timetableRepository;

  public User findOne(Long no) {
    return userRepository.findOne(no);
  }

  public User getUser(String id) {
    return userRepository.findOneById(id);
  }

  public ResponseEntity<ResponseMessage> findOneByNameOrId(String name) {
    ResponseMessage result;

    if (name == null) {
      result = new ResponseMessage(false, INVALID_INPUT);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    List<User> userList = userRepository.findByNameOrId(name, name);
    if (userList.size() == 0) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    List<UserDTO> userDTOList = new ArrayList<>();
    for (User user : userList) {
      UserDTO userDTO = new UserDTO(user);
      userDTOList.add(userDTO);
    }

    result = new ResponseMessage(true, userDTOList);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
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

  public ResponseEntity<ResponseMessage> updateRepresentTimetable(Long no, String token) {
    User user = sessionUser.getUserByToken(token);
    ResponseMessage result;

    if (user == null) {
      result = new ResponseMessage(false, INVALID_TOKEN);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.UNAUTHORIZED);
    }

    Timetable timetable = timetableRepository.findOne(no);

    if (timetable == null) {
      result = new ResponseMessage(false, NOT_FOUND_USER);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.NOT_FOUND);
    }

    if (user.getTimetable().getUser().getId() != timetable.getUser().getId()) {
      result = new ResponseMessage(false, CAN_NOT_UPDATE_OTHER_TIMETABLE);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    user.setTimetable(timetable);
    userRepository.save(user);
    result = new ResponseMessage(true, SUCCESS_UPDATE_REPRESENT_TIMETABLE);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
  }
}