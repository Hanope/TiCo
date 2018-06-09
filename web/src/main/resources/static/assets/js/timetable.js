// 크롤링 서버 전용 시간표 표시
// function createTable(result) {
//   days = result['message'];
//   day_index = 0;
//
//   for (var d in days) {
//     day = days[d];
//     timetables = day['timetables'];
//
//     for (var t in timetables) {
//       course = timetables[t];
//
//       course_time = course['time'];
//       course_name = course['course_name'];
//
//       selector = '.time[data-time="' + course_time + '"]';
//       $($(selector).siblings()[day_index]).addClass('purple');
//     }
//
//     day_index++;
//   }
// }
var no = window.location.pathname.split("/").pop();

function loadTimetableByUser() {
  $.ajax({
    url: '/api/timetable/' + no,
    type: 'GET',
    dataType: 'json',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('TiCo-Token', token);
    },
    success: function(response) {
      createTable(response['message'], 'purple');
    },
    error: function (response) {
      console.log(response['responseText']);
      alert('잘못된 접근입니다.');
      window.location.href = '/';
    }
  });
}

function loadTimetableByTeam() {
  $.ajax({
    url: '/api/team/' + no + '/timetable',
    type: 'GET',
    dataType: 'json',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('TiCo-Token', token);
    },
    success: function(response) {
      console.log(response);

      for (var i = 0; i < response['message'].length; i++) {
        createTable(response['message'][i], 'red');
      }
      $('#data_result').text(JSON.stringify(response, null, 4));
    }
  });
}

function createTable(response, color) {
  var schedules = response['schedules'];

  schedules.forEach(function(schedule, indexOfSchedule) {
    var dayIndex = dayToIndex(schedule['day']);

    schedule['hours'].forEach(function(hour, indexOfHour) {
      var hourCode = hour['hourCode'];
      var selector = '.time[data-time="' + hourCode + '"]';
      $($(selector).siblings()[dayIndex]).addClass(color);
    });
  });
}

function createTableOneSchedule(response, color) {
  var schedule = response['schedule'];
  var dayIndex = dayToIndex(schedule['day']);

  schedule['hours'].forEach(function(hour, indexOfHour) {
    var hourCode = hour['hourCode'];
    var selector = '.time[data-time="' + hourCode + '"]';
    $($(selector).siblings()[dayIndex]).addClass(color);
  });
}

function scheduleModalPopup(day, startTime, endTime) {
  $('#modal-schedule-text').val('');
  $('#modal-schedule-day').text(day);
  $('#modal-schedule-start-time').text(hourCodeToHour(startTime) + ' - ');
  $('#modal-schedule-start-hourCode').text(startTime);
  $('#modal-schedule-end-time').text(hourCodeToHour(endTime) + ' - ');
  $('#modal-schedule-end-hourCode').text(endTime);
  $('#modal-schedule').modal();
}

function scheduleModalPopupToTeam(day, date, startTime, endTime, location) {
  $('#modal-schedule-text').val('');
  $('#modal-schedule-day').text(day);
  $('#modal-schedule-date').text(date);
  $('#modal-schedule-start-time').text(hourCodeToHour(startTime) + ' - ');
  $('#modal-schedule-start-hourCode').text(startTime);
  $('#modal-schedule-end-time').text(hourCodeToHour(endTime) + ' - ');
  $('#modal-schedule-end-hourCode').text(endTime);
  $('#modal-schedule-location').text(location);
  $('#modal-schedule').modal();
}

$('#modal-schedule').on('hidden.bs.modal', function() {
  $('.green').removeClass();
});

function scheduleWrite() {
  var name = $('#modal-schedule-text').val();
  var day = $('#modal-schedule-day').text();
  var startHourCode = $('#modal-schedule-start-hourCode').text();
  var endHourCode = $('#modal-schedule-end-hourCode').text();
  var startIndex = hourCodeToIndex(startHourCode);
  var endIndex = hourCodeToIndex(endHourCode);
  var hours = [];

  for (; startIndex <= endIndex; startIndex++)
    hours.push(indexToHourCode(startIndex));

  var d = JSON.stringify({
    "name": name,
    "day": day,
    "hours": hours
  });

  console.log(d);

  $.ajax({
    url: '/api/schedule/' + no,
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json; charset=UTF-8',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('TiCo-Token', token);
    },
    data: JSON.stringify({
      "name": name,
      "day": day,
      "hours": hours
    }),
    success: function(response) {
      var result = response['result'];

      if (!result)
        alert(response['message']);
      else
        alert('시간표가 추가되었습니다.');

      location.reload();
    }
  });
}

function scheduleWriteToTeam(_date) {
  var title = $('#modal-schedule-title').val();
  var content = $('#modal-schedule-content').val();
  var day = $('#modal-schedule-day').text();
  var startHourCode = $('#modal-schedule-start-hourCode').text();
  var endHourCode = $('#modal-schedule-end-hourCode').text();
  var startIndex = hourCodeToIndex(startHourCode);
  var endIndex = hourCodeToIndex(endHourCode);
  var location_address = $('#select-location').val();
  var location_name = $('#select-location').find("option:selected").text();
  var hours = [];

  for (; startIndex <= endIndex; startIndex++)
    hours.push(indexToHourCode(startIndex));

  var d = JSON.stringify({
    "content": content,
    "date": _date,
    "location": {
      "address": location_address,
      "name": location_name
    },
    "schedule": {
        "day": day,
        "hours": hours,
        "name": content
      },
    "title": title
  });

  console.log(d);

  $.ajax({
    url: '/api/team/' + no + '/schedule',
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json; charset=UTF-8',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('TiCo-Token', token);
    },
    data: d,
    success: function(response) {
      var result = response['result'];

      if (!result)
        alert(response['message']);
      else
        alert('시간표가 추가되었습니다.');

      location.reload();
    }
  });
}


$('#search_btn').click(function(event) {
  event.preventDefault();
  user_id = $('#user_id').val();
  user_password = $('#user_password').val();

  $.ajax({
    url: 'http://localhost:8000/timetable',
    type: 'GET',
    dataType: 'json',
    data: {
      "user_id": user_id,
      "user_password": user_password
    },
    beforeSend: function(xhr) {
      xhr.setRequestHeader('TiCo-Token', token);
    },
    success: function(response) {
      // debug
      data = response;

      if (response['result'] === 'success')
        createTable(response);

      alert(response['message']);

      $('#data_result').text(JSON.stringify(response, null, 4));
    }
  });
});

function hourCodeToIndex(hourCode) {
  switch (hourCode) {
    case '1A': return 0; case '1B': return 1; case '2A': return 2; case '2B': return 3;
    case '3A': return 4; case '3B': return 5; case '4A': return 6; case '4B': return 7;
    case '5A': return 8; case '5B': return 9; case '6A': return 10; case '6B': return 11;
    case '7A': return 12; case '7B': return 13; case '8A': return 14; case '8B': return 15;
    case '9A': return 16; case '9B': return 17; case '10A': return 18; case '10B': return 19; case '11A': return 20;
    case '11B': return 21; case '12A': return 22; case '12B': return 23; case '13A': return 24; case '13B': return 25;
    case '14A': return 26;
  }
}

function indexToHourCode(index) {
  switch (index) {
    case 0: return '1A'; case 1: return '1B'; case 2: return '2A'; case 3: return '2B';
    case 4: return '3A'; case 5: return '3B'; case 6: return '4A'; case 7: return '4B';
    case 8: return '5A'; case 9: return '5B'; case 10: return '6A'; case 11: return '6B';
    case 12: return '7A'; case 13: return '7B'; case 14: return '8A'; case 15: return '8B';
    case 16: return '9A'; case 17: return '9B'; case 18: return '10A'; case 19: return '10B'; case 20: return '11A';
    case 21: return '11B'; case 22: return '12A'; case 23: return '12B'; case 24: return '13A'; case 25: return '13B';
    case 26: return '14A';
  }
}

function dayToIndex(day) {
  switch (day) {
    case 'MON': return 0;
    case 'TUE': return 1;
    case 'WED': return 2;
    case 'THU': return 3;
    case 'FRI': return 4;
    case 'SAT': return 5;
    case 'SUN': return 6;
  }
}

function indexToDay(index) {
  switch (index) {
    case 0: return 'MON';
    case 1: return 'TUE';
    case 2: return 'WED';
    case 3: return 'THU';
    case 4: return 'FRI';
    case 5: return 'SAT';
    case 6: return 'SUN';
  }
}

function hourCodeToHour(hourCode) {
  switch (hourCode) {
    case '1A': return '09:00'; case '1B': return '09:30';
    case '2A': return '10:00'; case '2B': return '10:30';
    case '3A': return '11:00'; case '3B': return '11:30';
    case '4A': return '12:00'; case '4B': return '12:30';
    case '5A': return '13:00'; case '5B': return '13:30';
    case '6A': return '14:00'; case '6B': return '14:30';
    case '7A': return '15:00'; case '7B': return '15:30';
    case '8A': return '16:00'; case '8B': return '16:30';
    case '9A': return '17:00'; case '9B': return '17:30';
    case '10A': return '18:00'; case '10B': return '18:30';
    case '11A': return '19:00'; case '11B': return '19:30';
    case '12A': return '20:00'; case '12B': return '20:30';
    case '13A': return '21:00'; case '13B': return '21:30';
    case '14A': return '22:00';
  }
}
