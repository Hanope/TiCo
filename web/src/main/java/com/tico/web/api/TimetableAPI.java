package com.tico.web.api;

import com.tico.web.domain.timetable.SyncTimetableDTO;
import com.tico.web.domain.timetable.Timetable;
import com.tico.web.domain.timetable.TimetableDTO;
import com.tico.web.service.TimetableService;
import com.tico.web.util.SessionUser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timetable")
public class TimetableAPI {

  @Autowired
  private TimetableService timetableService;

  @GetMapping("/{no}")
  public Timetable show(@PathVariable Long no) {
    return timetableService.findOne(no);
  }

  @PostMapping("/sync")
  public Map<String, Object> sync(@RequestBody SyncTimetableDTO timetableDTO) {
    System.out.println(timetableDTO);
    return timetableService.syncTimetable(timetableDTO);
  }
}
