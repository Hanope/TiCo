package com.tico.web.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.tico.web.repository.HourRepository;
import com.tico.web.repository.ScheduleRepository;
import com.tico.web.repository.TimetableRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleServiceTest {

  @Autowired
  private TimetableRepository timetableRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private HourRepository hourRepository;

  @Autowired
  private ScheduleService scheduleService;

//  @Test
//  @Transactional
//  public void 일정_추가_성공() {
//    List<Hour> hours = new ArrayList<>();
//    hours.add(hourRepository.findOne("5A"));
//    hours.add(hourRepository.findOne("5B"));
//    hours.add(hourRepository.findOne("6A"));
//    hours.add(hourRepository.findOne("6B"));
//    hours.add(hourRepository.findOne("7A"));
//    hours.add(hourRepository.findOne("7B"));
//
//    Schedule schedule = Schedule.builder()
//        .name("자바과외")
//        .day(Day.THU)
//        .hours(hours)
//        .build();
//
//    Timetable timetable = timetableRepository.findOne(1L);
//    Map<String, Object> resultSchedule = scheduleService.addSchedule(timetable, schedule);
//
//    assertThat(resultSchedule.get("result"), is(true));
//  }
//
//  @Test
//  @Transactional
//  public void 일정_추가_실패() {
//    List<Hour> hours = new ArrayList<>();
//    hours.add(hourRepository.findOne("1A"));
//    hours.add(hourRepository.findOne("1B"));
//    hours.add(hourRepository.findOne("2A"));
//    hours.add(hourRepository.findOne("2B"));
//    hours.add(hourRepository.findOne("3A"));
//    hours.add(hourRepository.findOne("3B"));
//
//    Schedule schedule = Schedule.builder()
//        .name("자바과외")
//        .day(Day.MON)
//        .hours(hours)
//        .build();
//
//    Timetable timetable = timetableRepository.findOne(1L);
//    Map<String, Object> resultSchedule = scheduleService.addSchedule(timetable, schedule);
//
//    assertThat(resultSchedule.get("result"), is(false));
//  }
}
