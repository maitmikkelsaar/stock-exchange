package ee.mikkelsaar.stockapi.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ee.mikkelsaar.stockapi.ConfigProperties;
import ee.mikkelsaar.stockapi.client.impl.NasdaqClientImpl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

class NasdaqClientTest {

  void renameMe(){
    String nasdaqUrl = "https://nasdaqbaltic.com/statistics/en/shares?download=1&date=";
    ConfigProperties configProperties = new ConfigProperties();
    configProperties.setNasdaqUrl(nasdaqUrl);

    RestTemplateBuilder restTemplate = Mockito.mock(RestTemplateBuilder.class);

    NasdaqClient nasdaqClient = new NasdaqClientImpl(configProperties, restTemplate);

    LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    when(restTemplate.build()).thenReturn(mockRestTemplate);
    doNothing().when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), byte[].class));

    nasdaqClient.getXlsx(localDateTime);


  }
}