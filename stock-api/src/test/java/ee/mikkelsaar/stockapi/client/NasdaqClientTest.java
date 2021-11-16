package ee.mikkelsaar.stockapi.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

import ee.mikkelsaar.stockapi.ConfigProperties;
import ee.mikkelsaar.stockapi.client.impl.NasdaqClientImpl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
class NasdaqClientTest {

  public static final String NASDAQ_URL = "https://nasdaqbaltic.com/statistics/en/shares?download=1&date=";
  @Captor
  ArgumentCaptor<String> urlCaptor;
  @Captor
  ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;

  @Test
  void getXlsx_success_url(){

    LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    String expectedUrl = NASDAQ_URL + String.format("%s-%s-%s", localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());

    ConfigProperties configProperties = new ConfigProperties();
    configProperties.setNasdaqUrl(NASDAQ_URL);

    RestTemplateBuilder restTemplate = Mockito.mock(RestTemplateBuilder.class);

    NasdaqClient nasdaqClient = new NasdaqClientImpl(configProperties, restTemplate);

    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    when(restTemplate.build()).thenReturn(mockRestTemplate);
    when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class))).thenReturn(mock(ResponseEntity.class));

    nasdaqClient.getXlsx(localDateTime);

    Mockito.verify(mockRestTemplate).exchange(urlCaptor.capture(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class));
    String actualUrl = urlCaptor.getValue();

    assertEquals(expectedUrl, actualUrl);
  }

  @Test
  void getXlsx_success_headers(){

    LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

    ConfigProperties configProperties = new ConfigProperties();
    configProperties.setNasdaqUrl(NASDAQ_URL);

    RestTemplateBuilder restTemplate = Mockito.mock(RestTemplateBuilder.class);

    NasdaqClient nasdaqClient = new NasdaqClientImpl(configProperties, restTemplate);

    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    when(restTemplate.build()).thenReturn(mockRestTemplate);
    when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class))).thenReturn(mock(ResponseEntity.class));

    nasdaqClient.getXlsx(localDateTime);

    Mockito.verify(mockRestTemplate).exchange(anyString(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(byte[].class));
    HttpEntity<String> actualHttpEntity = httpEntityCaptor.getValue();

    assertEquals(1, actualHttpEntity.getHeaders().size());
    assertNotNull(actualHttpEntity.getHeaders().get(HttpHeaders.ACCEPT));
    assertEquals(1, actualHttpEntity.getHeaders().get(HttpHeaders.ACCEPT).size());
    assertEquals(APPLICATION_OCTET_STREAM.toString(), actualHttpEntity.getHeaders().get(HttpHeaders.ACCEPT).stream().findFirst().get());
  }
}