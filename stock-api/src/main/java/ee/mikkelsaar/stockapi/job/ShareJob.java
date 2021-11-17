package ee.mikkelsaar.stockapi.job;

import static java.time.temporal.ChronoUnit.DAYS;

import ee.mikkelsaar.stockapi.service.DaysService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShareJob {

  private final DaysService daysService;

  @Scheduled(cron = "0 0/30 * * * *")
  public void sendReport() {

    LocalDateTime now = LocalDateTime.now().truncatedTo(DAYS);
    long daysToQuery = daysService.countDaysToQuery(now);

    for (long i = daysToQuery; i >= 0; i--) {
      daysService.getAndStoreDayData(now, i);
    }
  }
}
