package com.tico.web.api;

import com.tico.web.service.NaverAPIService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/naver")
public class NaverAPI {

  @Autowired
  private NaverAPIService naverService;

  @GetMapping("/local")
  public Map<String, Object> searchLocal(@RequestParam String localName) {
    return naverService.searchLocal(localName);
  }

}
