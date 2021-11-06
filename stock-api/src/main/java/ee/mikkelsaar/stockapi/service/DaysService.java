package ee.mikkelsaar.stockapi.service;

import ee.mikkelsaar.tables.pojos.Day;
import java.time.LocalDateTime;
import java.util.List;

public interface DaysService {

  List<Day> getDays();

  long countDaysToQuery(LocalDateTime dateTime);

  void getAndStoreDayData(LocalDateTime now, long nrOfDays);
}
