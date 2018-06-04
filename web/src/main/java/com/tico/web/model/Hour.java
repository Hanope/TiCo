package com.tico.web.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Hour {

  @Id
  private String hourCode;

  private LocalTime startHour;

  private LocalTime endHour;

  private Hour(String hourCode, LocalTime startHour, LocalTime endHour) {
    this.hourCode = hourCode;
    this.startHour = startHour;
    this.endHour = endHour;
  }

  public static List<Hour> stringHoursToListHour(List<String> hours) {
    List<Hour> hourList = new ArrayList<>();

    for (String hour : hours)
      hourList.add(Hour.getHour(hour));

    return hourList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Hour hour = (Hour) o;
    return Objects.equals(hourCode, hour.hourCode);
  }

  @Override
  public int hashCode() {

    return Objects.hash(hourCode);
  }

  public static Hour getHour(String hourCode) {
    LocalTime sHour = null;
    LocalTime eHour = null;

    switch (hourCode) {
      case "1A":
        sHour = LocalTime.of(9, 0); eHour = LocalTime.of(9, 30); break;
      case "1B":
        sHour = LocalTime.of(9, 30); eHour = LocalTime.of(10, 0); break;
      case "2A":
        sHour = LocalTime.of(10, 0); eHour = LocalTime.of(10, 30); break;
      case "2B":
        sHour = LocalTime.of(10, 30); eHour = LocalTime.of(11, 0); break;
      case "3A":
        sHour = LocalTime.of(11, 0); eHour = LocalTime.of(11, 30); break;
      case "3B":
        sHour = LocalTime.of(11, 30); eHour = LocalTime.of(12, 0); break;
      case "4A":
        sHour = LocalTime.of(12, 0); eHour = LocalTime.of(12, 30); break;
      case "4B":
        sHour = LocalTime.of(12, 30); eHour = LocalTime.of(13, 0); break;
      case "5A":
        sHour = LocalTime.of(13, 0); eHour = LocalTime.of(13, 30); break;
      case "5B":
        sHour = LocalTime.of(13, 30); eHour = LocalTime.of(14, 0); break;
      case "6A":
        sHour = LocalTime.of(14, 0); eHour = LocalTime.of(14, 30); break;
      case "6B":
        sHour = LocalTime.of(14, 30); eHour = LocalTime.of(15, 0); break;
      case "7A":
        sHour = LocalTime.of(15, 0); eHour = LocalTime.of(15, 30); break;
      case "7B":
        sHour = LocalTime.of(15, 30); eHour = LocalTime.of(16, 0); break;
      case "8A":
        sHour = LocalTime.of(16, 0); eHour = LocalTime.of(16, 30); break;
      case "8B":
        sHour = LocalTime.of(16, 30); eHour = LocalTime.of(17, 0); break;
      case "9A":
        sHour = LocalTime.of(17, 0); eHour = LocalTime.of(17, 30); break;
      case "9B":
        sHour = LocalTime.of(17, 30); eHour = LocalTime.of(18, 0); break;
      case "10A":
        sHour = LocalTime.of(18, 0); eHour = LocalTime.of(18, 30); break;
      case "10B":
        sHour = LocalTime.of(18, 30); eHour = LocalTime.of(19, 0); break;
      case "11A":
        sHour = LocalTime.of(19, 0); eHour = LocalTime.of(19, 30); break;
      case "11B":
        sHour = LocalTime.of(19, 30); eHour = LocalTime.of(20, 0); break;
      case "12A":
        sHour = LocalTime.of(20, 0); eHour = LocalTime.of(20, 30); break;
      case "12B":
        sHour = LocalTime.of(20, 30); eHour = LocalTime.of(21, 0); break;
      case "13A":
        sHour = LocalTime.of(21, 0); eHour = LocalTime.of(21, 30); break;
      case "13B":
        sHour = LocalTime.of(21, 30); eHour = LocalTime.of(22, 0); break;
      case "14A":
        sHour = LocalTime.of(22, 0); eHour = LocalTime.of(22, 30); break;
    }

    return new Hour(hourCode, sHour, eHour);
  }
}