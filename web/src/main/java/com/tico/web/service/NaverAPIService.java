package com.tico.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class NaverAPIService {

  public Map<String, Object> searchLocal(String localName) {
    Map<String, Object> result = new HashMap<>();
    JsonNode apiResult = requestAPI(localName);

    if (apiResult == null) {
      result.put("result", false);
      result.put("message", "Naver API 서버 오류");
      return result;
    }

    result.put("result", true);
    result.put("message", apiResult);

    return result;
  }

  public JsonNode requestAPI(String localName) {
    String clientId = "wnavBrZRPufq2v2I_OdJ";
    String clientSecret = "lnHsKbPeHb";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = null;

    try {
      String query = URLEncoder.encode(localName, "UTF-8");
      String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + query + "&display=30";
      URL url = new URL(apiURL);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("X-Naver-Client-Id", clientId);
      con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
      int responseCode = con.getResponseCode();

      BufferedReader br;
      if (responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }

      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }

      jsonNode = mapper.readTree(response.toString());
      br.close();
      System.out.println(response.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jsonNode;
  }
}
