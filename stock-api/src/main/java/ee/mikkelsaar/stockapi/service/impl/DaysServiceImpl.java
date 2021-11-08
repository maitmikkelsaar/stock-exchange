package ee.mikkelsaar.stockapi.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import ee.mikkelsaar.stockapi.dao.jooq.DayJooqDao;
import ee.mikkelsaar.stockapi.model.Details;
import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.service.DaysService;
import ee.mikkelsaar.stockapi.service.ShareService;
import ee.mikkelsaar.tables.daos.DayDao;
import ee.mikkelsaar.tables.pojos.Day;
import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DaysServiceImpl implements DaysService {

  private final DayDao dayDao;
  private final DayJooqDao dayJooqDao;
  private final ShareService shareService;

  @Override
  public List<Day> getDays() {
    return dayDao.findAll();
  }

  @Override
  public long countDaysToQuery(final LocalDateTime dateTime) {

    Optional<LocalDateTime> lastDate = getLastDate();

    if (lastDate.isPresent()) {
      long daysBetween = DAYS.between(lastDate.get(), dateTime);
      return Math.min(daysBetween, 9L);
    } else {
      return 9L;
    }
  }

  public Optional<LocalDateTime> getLastDate() {
    return dayJooqDao.getLastDate();
  }

  @Override
  public void getAndStoreDayData(final LocalDateTime now, final long nrOfDays) {
    final LocalDateTime toGet = now.minusDays(nrOfDays);

    final List<Share> shareList = shareService.fetchShares(toGet);

    dayJooqDao.upsertDayWithShares(toGet, shareList);
  }

  public Details getDetails(Long dayId) {
    List<Share> allByDay = shareService.getAllByDay(dayId);

    GainersDecliners gainersDecliners = shareService.getGainersDecliners(allByDay);
    List<ShareValue> mostActive = shareService.mostActive(allByDay);
    List<ShareValue> biggestTurnover = shareService.biggestTurnover(allByDay);

    return new Details(gainersDecliners, mostActive, biggestTurnover);
  }
}
