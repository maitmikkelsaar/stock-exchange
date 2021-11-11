package ee.mikkelsaar.stockapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.mikkelsaar.stockapi.dao.jooq.DayJooqDao;
import ee.mikkelsaar.stockapi.service.impl.DaysServiceImpl;
import ee.mikkelsaar.stockapi.service.impl.SharesServiceImpl;
import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DaysServiceTest {

  @Test
  void countDaysToQuery_today() {

    LocalDateTime now = LocalDateTime.now();

    DayJooqDao dayJooqDao = Mockito.mock(DayJooqDao.class);
    DaysService daysService = new DaysServiceImpl(null, dayJooqDao, null);

    when(dayJooqDao.getLastDate()).thenReturn(Optional.of(now));

    long count = daysService.countDaysToQuery(now);
    assertEquals(0, count);
  }

  @Test
  void countDaysToQuery_noData() {

    LocalDateTime now = LocalDateTime.now();

    DayJooqDao dayJooqDao = Mockito.mock(DayJooqDao.class);
    DaysService daysService = new DaysServiceImpl(null, dayJooqDao, null);

    when(dayJooqDao.getLastDate()).thenReturn(Optional.empty());

    long count = daysService.countDaysToQuery(now);
    assertEquals(9, count);
  }

  @Test
  void countDaysToQuery_10Days() {

    LocalDateTime now = LocalDateTime.now();

    DayJooqDao dayJooqDao = Mockito.mock(DayJooqDao.class);
    DaysService daysService = new DaysServiceImpl(null, dayJooqDao, null);

    when(dayJooqDao.getLastDate()).thenReturn(Optional.of(now.minusDays(10)));

    long count = daysService.countDaysToQuery(now);
    assertEquals(9, count);
  }

  @Test
  void countDaysToQuery_5Days() {

    LocalDateTime now = LocalDateTime.now();

    DayJooqDao dayJooqDao = Mockito.mock(DayJooqDao.class);
    DaysService daysService = new DaysServiceImpl(null, dayJooqDao, null);

    when(dayJooqDao.getLastDate()).thenReturn(Optional.of(now.minusDays(5)));

    long count = daysService.countDaysToQuery(now);
    assertEquals(5, count);
  }

  @Test
  void getAndStoreDayData() {

    DayJooqDao dayJooqDao = Mockito.mock(DayJooqDao.class);
    SharesService sharesService = Mockito.mock(SharesServiceImpl.class);
    DaysService daysService = new DaysServiceImpl(null, dayJooqDao, sharesService);

    LocalDateTime time = LocalDateTime.now();
    int nrOfDays = 2;
    LocalDateTime timeToGet = time.minusDays(nrOfDays);
    List<Share> list = List.of(new Share());

    when(sharesService.fetchShares(eq(timeToGet))).thenReturn(list);

    daysService.getAndStoreDayData(time, nrOfDays);

    verify(dayJooqDao).upsertDayWithShares(timeToGet, list);
  }
}