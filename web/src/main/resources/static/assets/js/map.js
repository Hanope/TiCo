var map = new naver.maps.Map('map');
// var myaddress = '대구광역시 북구 대학로 75';
// naver.maps.Service.geocode({address: myaddress}, function(status, response) {
//   if (status !== naver.maps.Service.Status.OK) {
//     return alert(myaddress + '의 검색 결과가 없거나 기타 네트워크 에러');
//   }
//   var result = response.result;
//   var myaddr = new naver.maps.Point(result.items[0].point.x, result.items[0].point.y);
//   map.setCenter(myaddr);
//
//   var marker = new naver.maps.Marker({
//     position: myaddr,
//     map: map
//   });
//   naver.maps.Event.addListener(marker, "click", function(e) {
//     if (infowindow.getMap()) {
//       infowindow.close();
//     } else {
//       infowindow.open(map, marker);
//     }
//   });
//   // 마크 클릭시 인포윈도우 오픈
//   var infowindow = new naver.maps.InfoWindow({
//     content: '<h6>스타벅스</h6>'
//   });
// });