package com.tico.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tico.web.model.ResponseMessage;
import com.tico.web.model.ResponseStatus;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NaverAPIService {

  public ResponseEntity<ResponseMessage> searchLocal(String localName) {
    ResponseMessage result;
    JsonNode apiResult = requestAPI(localName);

    if (apiResult == null) {
      result = new ResponseMessage(false, ResponseStatus.SERVER_ERROR);
      return new ResponseEntity<ResponseMessage>(result, HttpStatus.FORBIDDEN);
    }

    result = new ResponseMessage(false, apiResult);
    return new ResponseEntity<ResponseMessage>(result, HttpStatus.OK);
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
