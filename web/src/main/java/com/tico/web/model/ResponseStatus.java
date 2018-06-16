package com.tico.web.model;

public interface ResponseStatus {

  String INVALID_INPUT = "입력이 올바르지 않습니다.";

  String NOT_FOUND_USER = "존재하지 않는 회원입니다.";

  String NOT_FOUND_PROJECT = "프로젝트가 존재하지 않습니다.";

  String NOT_FOUND_TIMETABLE = "시간표가 존재하지 않습니다.";

  String NOT_FOUND_SCHEDULE = "일정이 존재하지 않습니다.";

  String EXISTS_SCHEDULE = "이미 중복된 일정이 존재합니다.";

  String EXISTS_MEMBER = "이미 존재하는 회원입니다.";

  String CAN_NOT_UPDATE_OTHER_TIMETABLE = "다른 사람의 시간표를 수정할 수 없습니다.";

  String CAN_NOT_UPDATE_PROJECT_MEMBER = "프로젝트 개설자만 관리할 수 있습니다.";

  String CAN_NOT_DELETE_PROJECT_OWNER = "프로젝트 개설자는 삭제할 수 없습니다.";

  String SUCCESS_ADD_SCHEDULE = "일정이 추가되었습니다.";

  String SUCCESS_UPDATE_REPRESENT_TIMETABLE = "대표 시간표로 설정되었습니다";

  String SUCCESS_DELETE_SCHEDULE = "일정이 삭제되었습니다.";

  String SERVER_ERROR = "Server Error";

  String INVALID_TOKEN = "토큰이 유효하지 않습니다.";

}
