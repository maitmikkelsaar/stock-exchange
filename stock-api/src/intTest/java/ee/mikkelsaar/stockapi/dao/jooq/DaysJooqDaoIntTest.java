package ee.mikkelsaar.stockapi.dao.jooq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ee.mikkelsaar.stockapi.AbstractIntTest;
import ee.mikkelsaar.tables.daos.DayDao;
import ee.mikkelsaar.tables.daos.ShareDao;
import ee.mikkelsaar.tables.pojos.Day;
import ee.mikkelsaar.tables.pojos.Share;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class DaysJooqDaoIntTest extends AbstractIntTest {

  @Autowired
  private DaysJooqDao daysJooqDao;
  @Autowired
  private ShareDao shareDao;
  @Autowired
  private DayDao dayDao;
  @Autowired
  private DSLContext dsl;

  @Test
  void getLastDate() {

    Optional<LocalDateTime> lastDateOp = daysJooqDao.getLastDate();
    LocalDateTime lastDate = lastDateOp.orElse(LocalDateTime.now());

    LocalDateTime newDate = lastDate.plusDays(1);
    Day dayDb = daysJooqDao.insert(newDate, dsl.configuration());

    Optional<LocalDateTime> newLastDate = daysJooqDao.getLastDate();
    assertEquals(dayDb.getDate(), newLastDate.get());
  }

  @Test
  void upsertDayWithShares_success_insert() {

    LocalDateTime lastDate = daysJooqDao.getLastDate().orElse(LocalDateTime.now());

    Share share1 = getShare("AAAA");
    Share share2 = getShare("BBBB");

    LocalDateTime newDate = lastDate.plusDays(1);
    List<Share> insertShares = List.of(share1, share2);

    Day day = daysJooqDao.upsertDayWithShares(newDate, insertShares);

    assertEquals(newDate, day.getDate());

    List<Share> sharesDb = shareDao.fetchByDay(day.getId());
    assertEquals(insertShares.size(), sharesDb.size());
    assertTrue(sharesDb.stream().anyMatch(share -> share.getTicker().equals(share1.getTicker())));
    assertTrue(sharesDb.stream().anyMatch(share -> share.getTicker().equals(share2.getTicker())));
  }

  @Test
  void upsertDayWithShares_success_update() {

    LocalDateTime lastDate = daysJooqDao.getLastDate().orElse(LocalDateTime.now());

    Share share1 = getShare("AAAA");
    Share share2 = getShare("BBBB");
    Share share3 = getShare("CCCC");

    LocalDateTime newDate = lastDate.plusDays(1);
    List<Share> insertShares = List.of(share1, share2);

    Day day = daysJooqDao.upsertDayWithShares(newDate, insertShares);

    List<Share> sharesDb = shareDao.fetchByDay(day.getId());
    assertEquals(insertShares.size(), sharesDb.size());

    List<Share> updateShares = List.of(share1, share2, share3);
    daysJooqDao.upsertDayWithShares(newDate, updateShares);

    sharesDb = shareDao.fetchByDay(day.getId());
    assertEquals(updateShares.size(), sharesDb.size());
    assertTrue(sharesDb.stream().anyMatch(share -> share.getTicker().equals(share1.getTicker())));
    assertTrue(sharesDb.stream().anyMatch(share -> share.getTicker().equals(share2.getTicker())));
    assertTrue(sharesDb.stream().anyMatch(share -> share.getTicker().equals(share3.getTicker())));
  }

  @Test
  void upsertDayWithShares_exception_failingShareInsertDoesNotInsertDay() {

    LocalDateTime lastDate = daysJooqDao.getLastDate().orElse(LocalDateTime.now());

    Share share1 = getShareWithTickerAsName("AAAAAAAAA");

    LocalDateTime newDate = lastDate.plusDays(1);
    List<Share> insertShares = List.of(share1);

    int daysSize = dayDao.findAll().size();
    int sharesSize = shareDao.findAll().size();

    assertThrows(DataIntegrityViolationException.class, () -> daysJooqDao.upsertDayWithShares(newDate, insertShares));

    int daysSizeAfterUpsert = dayDao.findAll().size();
    int sharesSizeAfterUpsert = shareDao.findAll().size();
    assertEquals(daysSize, daysSizeAfterUpsert);
    assertEquals(sharesSize, sharesSizeAfterUpsert);
  }

  private Share getShareWithTickerAsName(final String ticker) {
    return getShare(ticker, ticker);
  }

  private Share getShare(final String ticker) {
    return getShare(ticker, "Name");
  }

  private Share getShare(final String ticker, final  String name) {
    Share share = new Share();
    share.setTicker(ticker);
    share.setName(name);
    share.setIsin("Isin");
    share.setCurrency("EUR");
    share.setMarketplace("TLN");
    share.setList("List");
    share.setAveragePrice(BigDecimal.TEN);
    share.setOpenPrice(BigDecimal.TEN);
    share.setHighPrice(BigDecimal.TEN);
    share.setLowPrice(BigDecimal.TEN);
    share.setLastClosePrice(BigDecimal.TEN);
    share.setLastPrice(BigDecimal.TEN);
    share.setPriceChangePercentage(BigDecimal.TEN);
    share.setBestBid(BigDecimal.TEN);
    share.setBestAsk(BigDecimal.TEN);
    share.setTrades(1);
    share.setVolume(1L);
    share.setTurnover(BigDecimal.TEN);
    share.setIndustry("Industry");
    share.setSupersector("Supersector");
    return share;
  }
}
