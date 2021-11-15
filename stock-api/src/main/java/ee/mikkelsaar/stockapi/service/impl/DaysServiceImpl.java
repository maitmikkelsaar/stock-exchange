package ee.mikkelsaar.stockapi.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import ee.mikkelsaar.stockapi.dao.jooq.DaysJooqDao;
import ee.mikkelsaar.stockapi.model.Details;
import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.service.DaysService;
import ee.mikkelsaar.stockapi.service.SharesService;
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
  private final DaysJooqDao daysJooqDao;
  private final SharesService sharesService;

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
    return daysJooqDao.getLastDate();
  }

  @Override
  public void getAndStoreDayData(final LocalDateTime now, final long nrOfDays) {
    final LocalDateTime toGet = now.minusDays(nrOfDays);

    final List<Share> shareList = sharesService.fetchShares(toGet);

    daysJooqDao.upsertDayWithShares(toGet, shareList);
  }

  public Details getDetails(Long dayId) {
    List<Share> allByDay = sharesService.getAllByDay(dayId);

    GainersDecliners gainersDecliners = sharesService.getGainersDecliners(allByDay);
    List<ShareValue> mostActive = sharesService.getMostActive(allByDay);
    List<ShareValue> biggestTurnover = sharesService.getBiggestTurnover(allByDay);

    return new Details(gainersDecliners, mostActive, biggestTurnover);
  }
}
