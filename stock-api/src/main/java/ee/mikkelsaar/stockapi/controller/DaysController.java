package ee.mikkelsaar.stockapi.controller;

import ee.mikkelsaar.stockapi.service.DaysService;
import ee.mikkelsaar.tables.pojos.Day;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "${settings.cors_origin}")
@RequiredArgsConstructor
@RestController
@RequestMapping("days")
public class DaysController {

  private final DaysService daysService;

  @GetMapping
  public List<Day> getDays() {
    return daysService.getDays();
  }


}
