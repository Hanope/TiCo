var element = document.getElementById("my-calendar");
var myCalendar = jsCalendar.new(element);
var previousDate = '';
var clickDay = '';
var clickMonth = '';

$.ajax({
  url: '/api/team/' + no + '/schedule',
  method: 'get',
  dataType: 'json',
  success: function(response) {
    var data = response['message'];
    var scheduledDate = [];

    if (response['result'] == false)
      return;

    for (var i = 0; i < data.length; i++)
      scheduledDate.push(data[i]['date']);
    myCalendar.select(scheduledDate);

    console.log(response);
  }
});

myCalendar.onDateClick(function(event, date) {
  var gYear = date.getFullYear();
  var gMonth = date.getMonth() + 1;
  var gDate = date.getDate();
  var d = pad(gDate, 2) + '/' + pad(gMonth, 2) + '/' + gYear;

  clickDay = date.toString();
  if (previousDate != '')
    myCalendar.unselect([previousDate]);
  myCalendar.select([d]);

  previousDate = d;
});
myCalendar.onMonthChange(function(event, date) {
  clickMonth = date.toString();
});

function pad(n, width) {
  n = n + '';
  return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}