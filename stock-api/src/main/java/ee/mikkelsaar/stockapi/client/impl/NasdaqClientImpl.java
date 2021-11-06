package ee.mikkelsaar.stockapi.client.impl;

import ee.mikkelsaar.stockapi.ConfigProperties;
import ee.mikkelsaar.stockapi.client.NasdaqClient;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NasdaqClientImpl implements NasdaqClient {

  private final ConfigProperties configProperties;
  private final RestTemplateBuilder restTemplate;

  @Override
  public byte[] getXlsx(final LocalDateTime localDateTime) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String url = String.format(configProperties.getNasdaqUrl() + "%s-%s-%s", localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
    ResponseEntity<byte[]> response = restTemplate.build()
        .exchange(url, HttpMethod.GET, entity, byte[].class);
    return response.getBody();
  }

}
