package com.tico.web.api;

import com.tico.web.model.ResponseMessage;
import com.tico.web.service.NaverAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/api/naver")
public class NaverAPI {

  @Autowired
  private NaverAPIService naverService;

  @GetMapping("/location")
  public ResponseEntity<ResponseMessage> searchLocal(@RequestParam String localName) {
    return naverService.searchLocal(localName);
  }

}
