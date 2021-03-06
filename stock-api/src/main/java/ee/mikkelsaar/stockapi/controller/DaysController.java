package ee.mikkelsaar.stockapi.controller;

import ee.mikkelsaar.stockapi.model.Details;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.stockapi.service.DaysService;
import ee.mikkelsaar.stockapi.service.SharesService;
import ee.mikkelsaar.tables.pojos.Day;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("days")
public class DaysController {

  private final DaysService daysService;
  private final SharesService sharesService;

  @GetMapping
  public List<Day> getDays() {
    return daysService.getDays();
  }

  @GetMapping("/{id}/detail")
  public Details getDetails(@PathVariable Long id) {
    return daysService.getDetails(id);
  }

  @PostMapping("/detail/volume")
  public List<ShareValue> getVolume(@RequestBody TimeRangeRequest timeRangeRequest) {
    return sharesService.getTimeRangeVolumes(timeRangeRequest);
  }
}
