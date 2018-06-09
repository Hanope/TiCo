package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.model.timetable.SyncTimetableDTO;
import com.tico.web.service.TimetableService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timetable")
public class TimetableAPI {

  @Autowired
  private TimetableService timetableService;

  @GetMapping("/{no}")
  public ResponseEntity<ResponseMessage> show(@PathVariable Long no, @RequestHeader(value="TiCo-Token") String token) {
    return timetableService.findOne(no, token);
  }

  @PostMapping("/sync")
  public ResponseEntity<ResponseMessage> sync(@RequestBody SyncTimetableDTO timetableDTO) {
    return timetableService.syncTimetable(timetableDTO);
  }
}
