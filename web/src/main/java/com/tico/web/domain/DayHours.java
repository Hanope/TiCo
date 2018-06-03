package com.tico.web.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayHours {

  private Day day;

  private List<Hour> hours;
}
