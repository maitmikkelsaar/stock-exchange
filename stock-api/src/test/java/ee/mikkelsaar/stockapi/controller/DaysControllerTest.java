package ee.mikkelsaar.stockapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.mikkelsaar.stockapi.model.Details;
import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.stockapi.service.DaysService;
import ee.mikkelsaar.stockapi.service.SharesService;
import ee.mikkelsaar.tables.pojos.Day;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(Lifecycle.PER_CLASS)
public class DaysControllerTest {

  public static final String DAYS_PATH = "/days";
  private MockMvc mockMvc;

  @Mock
  private DaysService daysService;

  @Mock
  private SharesService sharesService;

  @InjectMocks
  private DaysController daysController;

  @BeforeAll
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.mockMvc =  MockMvcBuilders.standaloneSetup(daysController).build();
  }

  @Test
  public void getDays_success_emptyList() throws Exception {

    List<Day> dayList = List.of();
    Mockito.when(daysService.getDays()).thenReturn(dayList);

    MvcResult result = mockMvc.perform(get(DAYS_PATH))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    assertEquals(0, result.getResponse().getContentLength());
  }

  @Test
  public void getDays_success_2Days() throws Exception {

    Day day1 = getDay(1L, LocalDateTime.now().minusDays(1));
    Day day2 = getDay(2L, LocalDateTime.now());

    List<Day> expectedDayList = List.of(day1, day2);
    Mockito.when(daysService.getDays()).thenReturn(expectedDayList);

    MvcResult result = mockMvc.perform(get(DAYS_PATH))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    final String content = result.getResponse().getContentAsString();
    final List<Day> daysResponse = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .readValue(content, new TypeReference<>() {});

    assertEquals(expectedDayList, daysResponse);
  }

  @Test
  public void getDetails_success() throws Exception {

    long dayId = 1L;
    Details expectedDetails = getDetails();

    Mockito.when(daysService.getDetails(dayId))
        .thenReturn(expectedDetails);

    String url = String.format(DAYS_PATH + "/%s/detail", dayId);
    MvcResult result = mockMvc.perform(get(url))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    final String content = result.getResponse().getContentAsString();
    final Details detailsResponse = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .readValue(content, Details.class);

    assertEquals(expectedDetails, detailsResponse);
  }

  @Test
  public void getVolume_success() throws Exception {

    TimeRangeRequest timeRangeRequest = new TimeRangeRequest(LocalDateTime.now().minusDays(1), LocalDateTime.now());

    List<ShareValue> expectedShareValues = List.of(
        new ShareValue("A", BigDecimal.TEN),
        new ShareValue("B", BigDecimal.TEN));
    Mockito.when(sharesService.getTimeRangeVolumes(timeRangeRequest))
        .thenReturn(expectedShareValues);

    MvcResult result = mockMvc.perform(
            post(DAYS_PATH + "/detail/volume")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(timeRangeRequest)))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    final String content = result.getResponse().getContentAsString();
    final List<ShareValue> shareVolumesResponse = new ObjectMapper()
        .readValue(content, new TypeReference<>() {});

    assertEquals(expectedShareValues, shareVolumesResponse);
  }

  private Day getDay(long l, LocalDateTime dateTime) {
    return new Day()
        .setId(l)
        .setDate(dateTime);
  }

  private Details getDetails() {
    List<ShareValue> gainers = getShareValueListOf3("A", "5", "B", "4", "C", "3");
    List<ShareValue> decliners = getShareValueListOf3("D", "-2", "E", "-3", "F", "-4");
    GainersDecliners gainersDecliners = new GainersDecliners(gainers, decliners);
    List<ShareValue> mostActive = getShareValueListOf6("A", "300", "G", "250", "B", "200", "C", "175", "H", "150", "I", "100");
    List<ShareValue> biggestTurnover = getShareValueListOf6("A", "4000", "G", "3500", "B", "3000", "H", "2850", "I", "1700", "J", "1020");
    return new Details(gainersDecliners, mostActive, biggestTurnover);
  }

  private List<ShareValue> getShareValueListOf6(String name1, String value1, String name2, String value2, String name3, String value3,
      String name4, String value4, String name5, String value5, String name6, String value6) {
    return List.of(
        new ShareValue(name1, new BigDecimal(value1)),
        new ShareValue(name2, new BigDecimal(value2)),
        new ShareValue(name3, new BigDecimal(value3)),
        new ShareValue(name4, new BigDecimal(value4)),
        new ShareValue(name5, new BigDecimal(value5)),
        new ShareValue(name6, new BigDecimal(value6)));
  }

  private List<ShareValue> getShareValueListOf3(String name1, String value1, String name2, String value2, String name3, String value3) {
    return List.of(
        new ShareValue(name1, new BigDecimal(value1)),
        new ShareValue(name2, new BigDecimal(value2)),
        new ShareValue(name3, new BigDecimal(value3)));
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}