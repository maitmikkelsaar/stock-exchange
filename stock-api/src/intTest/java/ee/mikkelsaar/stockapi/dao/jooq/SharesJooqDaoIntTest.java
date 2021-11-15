package ee.mikkelsaar.stockapi.dao.jooq;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ee.mikkelsaar.stockapi.AbstractIntTest;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.tables.daos.ShareDao;
import ee.mikkelsaar.tables.pojos.Day;
import ee.mikkelsaar.tables.pojos.Share;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SharesJooqDaoIntTest extends AbstractIntTest {

  @Autowired
  private DaysJooqDao daysJooqDao;
  @Autowired
  private ShareDao shareDao;
  @Autowired
  private SharesJooqDao sharesJooqDao;
  @Autowired
  private DSLContext dsl;

  @Test
  void insert() {

    Day day = daysJooqDao.insert(LocalDateTime.now(), dsl.configuration());

    Share share = getShare(day, "Name", "AAAA1", 100L);

    Share shareDb = sharesJooqDao.insert(share);

    assertEquals(shareDb, shareDao.findById(shareDb.getId()));
  }

  @Test
  void upsert_success_insert() {

    Day day = daysJooqDao.insert(LocalDateTime.now(), dsl.configuration());

    Share share = getShare(day, "Name", "AAAA1", 100L);

    List<Share> emptySharesDb = shareDao.fetchByDay(day.getId());
    assertTrue(emptySharesDb.isEmpty());

    sharesJooqDao.upsert(List.of(share), dsl.configuration());

    List<Share> sharesDb = shareDao.fetchByDay(day.getId());
    assertEquals(1, sharesDb.size());

    Share shareDb = sharesDb.stream().findFirst().get();

    assertNotNull(shareDb);
    assertShare(shareDb, share);
  }

  @Test
  void upsert_success_update() {

    Day day = daysJooqDao.insert(LocalDateTime.now(), dsl.configuration());

    Share share = getShare(day, "Name", "AAAA1", 100L);

    Share shareDb = sharesJooqDao.insert(share);

    int newTradesValue = 2;
    share.setTrades(newTradesValue);

    sharesJooqDao.upsert(List.of(share), dsl.configuration());

    Share upsertShare = shareDao.findById(shareDb.getId());

    assertNotNull(upsertShare);
    assertNotEquals(shareDb, upsertShare);
    assertEquals(shareDb.getId(), upsertShare.getId());
    assertNotEquals(shareDb.getTrades(), upsertShare.getTrades());
    assertEquals(newTradesValue, upsertShare.getTrades());
  }

  @Test
  void findTopVolumesInRange_success_top10() {

    Optional<LocalDateTime> lastDateOp = daysJooqDao.getLastDate();
    LocalDateTime lastDate = lastDateOp.orElse(LocalDateTime.now().truncatedTo(DAYS));

    LocalDateTime day1Date = lastDate.plusDays(1);
    LocalDateTime day2Date = lastDate.plusDays(2);

    Day day1 = daysJooqDao.insert(day1Date, dsl.configuration());
    Day day2 = daysJooqDao.insert(day2Date, dsl.configuration());

    String inTopN1 = "N1";
    String inTopN2 = "N2";
    String inTopN3 = "N3";
    String inTopN4 = "N4";
    String inTopN5 = "N5";
    String inTopN6 = "N6";
    String notTop10N7 = "N7";
    String inTopN8 = "N8";
    String inTopN9 = "N9";
    String inTopN10 = "N10";
    String inTopN11 = "N11";
    Share share1 = getShareNameAsTicker(day1, inTopN1, 1000L);
    Share share2 = getShareNameAsTicker(day1, inTopN2, 300L);
    Share share3 = getShareNameAsTicker(day1, inTopN3, 1100L);
    Share share4 = getShareNameAsTicker(day2, inTopN4, 600L);
    Share share5 = getShareNameAsTicker(day2, inTopN5, 500L);
    Share share6 = getShareNameAsTicker(day2, inTopN6, 800L);
    Share share7 = getShareNameAsTicker(day2, notTop10N7, 100L);
    Share share8 = getShareNameAsTicker(day2, inTopN8, 700L);
    Share share9 = getShareNameAsTicker(day2, inTopN9, 900L);
    Share share10 = getShareNameAsTicker(day2, inTopN10, 200L);
    Share share11 = getShareNameAsTicker(day2, inTopN11, 400L);

    List<Share> shareList = List.of(share1, share2, share3, share4, share5, share6, share7, share8,
        share9, share10, share11);
    shareDao.insert(shareList);

    TimeRangeRequest timeRangeRequest = new TimeRangeRequest();
    timeRangeRequest.setStart(day1Date);
    timeRangeRequest.setEnd(day2Date);

    List<ShareValue> allInRange = sharesJooqDao.findTopVolumesInRange(timeRangeRequest);

    assertNullShareValue(allInRange, notTop10N7);
    assertShareValue(allInRange, inTopN1, share1.getVolume());
    assertShareValue(allInRange, inTopN2, share2.getVolume());
    assertShareValue(allInRange, inTopN3, share3.getVolume());
    assertShareValue(allInRange, inTopN4, share4.getVolume());
    assertShareValue(allInRange, inTopN5, share5.getVolume());
    assertShareValue(allInRange, inTopN6, share6.getVolume());
    assertShareValue(allInRange, inTopN8, share8.getVolume());
    assertShareValue(allInRange, inTopN9, share9.getVolume());
    assertShareValue(allInRange, inTopN10, share10.getVolume());
    assertShareValue(allInRange, inTopN11, share11.getVolume());
  }

  @Test
  void findTopVolumesInRange_success_aggregateOverDays() {

    Optional<LocalDateTime> lastDateOp = daysJooqDao.getLastDate();
    LocalDateTime lastDate = lastDateOp.orElse(LocalDateTime.now().truncatedTo(DAYS));

    LocalDateTime day1Date = lastDate.plusDays(1);
    LocalDateTime day2Date = lastDate.plusDays(2);
    LocalDateTime day3Date = lastDate.plusDays(3);
    LocalDateTime day4Date = lastDate.plusDays(4);

    Day day1 = daysJooqDao.insert(day1Date, dsl.configuration());
    Day day2 = daysJooqDao.insert(day2Date, dsl.configuration());
    Day day3 = daysJooqDao.insert(day3Date, dsl.configuration());
    Day day4 = daysJooqDao.insert(day4Date, dsl.configuration());

    String aggregateableN1 = "N1";
    String notInRangeN2 = "N2";
    String aggregateableN3 = "N3";
    String singleValueN4 = "N4";
    String singleValueN5 = "N5";
    String notInRangeN6 = "N6";
    Share share1 = getShareNameAsTicker(day1, aggregateableN1, 100L);
    Share share2 = getShareNameAsTicker(day1, notInRangeN2, 200L);
    Share share3 = getShareNameAsTicker(day2, aggregateableN1, 300L);
    Share share4 = getShareNameAsTicker(day2, aggregateableN3, 400L);
    Share share5 = getShareNameAsTicker(day3, aggregateableN1, 500L);
    Share share6 = getShareNameAsTicker(day3, aggregateableN3, 600L);
    Share share7 = getShareNameAsTicker(day3, singleValueN4, 700L);
    Share share8 = getShareNameAsTicker(day3, singleValueN5, 800L);
    Share share9 = getShareNameAsTicker(day4, aggregateableN3, 900L);
    Share share10 = getShareNameAsTicker(day4, aggregateableN1, 1000L);
    Share share11 = getShareNameAsTicker(day4, notInRangeN6, 1100L);

    List<Share> shareList = List.of(share1, share2, share3, share4, share5, share6, share7, share8, share9, share10,
        share11);
    shareDao.insert(shareList);

    TimeRangeRequest timeRangeRequest = new TimeRangeRequest();
    timeRangeRequest.setStart(day2Date);
    timeRangeRequest.setEnd(day3Date);

    List<ShareValue> allInRange = sharesJooqDao.findTopVolumesInRange(timeRangeRequest);

    assertEquals(4, allInRange.size());
    assertNullShareValue(allInRange, notInRangeN2);
    assertNullShareValue(allInRange, notInRangeN6);
    assertShareValue(allInRange, aggregateableN1, share3.getVolume(), share5.getVolume());
    assertShareValue(allInRange, aggregateableN3, share4.getVolume(), share6.getVolume());
    assertShareValue(allInRange, singleValueN4, share7.getVolume());
    assertShareValue(allInRange, singleValueN5, share8.getVolume());
  }

  @Test
  void findTopVolumesInRange_success_notInTimeRange() {

    Optional<LocalDateTime> lastDateOp = daysJooqDao.getLastDate();
    LocalDateTime lastDate = lastDateOp.orElse(LocalDateTime.now().truncatedTo(DAYS));

    LocalDateTime day1Date = lastDate.plusDays(1);
    LocalDateTime day2Date = lastDate.plusDays(2);
    LocalDateTime day3Date = lastDate.plusDays(3);
    LocalDateTime day4Date = lastDate.plusDays(4);

    Day day1 = daysJooqDao.insert(day1Date, dsl.configuration());
    Day day2 = daysJooqDao.insert(day2Date, dsl.configuration());
    Day day3 = daysJooqDao.insert(day3Date, dsl.configuration());
    Day day4 = daysJooqDao.insert(day4Date, dsl.configuration());

    String norInRangeN1 = "N1";
    String inRangeN2 = "N2";
    String inRangeN3 = "N3";
    String notInRangeN4 = "N4";
    Share share1 = getShareNameAsTicker(day1, norInRangeN1, 100L);
    Share share2 = getShareNameAsTicker(day2, inRangeN2, 200L);
    Share share3 = getShareNameAsTicker(day3, inRangeN3, 300L);
    Share share4 = getShareNameAsTicker(day4, notInRangeN4, 400L);

    List<Share> shareList = List.of(share1, share2, share3, share4);
    shareDao.insert(shareList);

    TimeRangeRequest timeRangeRequest = new TimeRangeRequest();
    timeRangeRequest.setStart(day2Date);
    timeRangeRequest.setEnd(day3Date);

    List<ShareValue> allInRange = sharesJooqDao.findTopVolumesInRange(timeRangeRequest);

    assertEquals(2, allInRange.size());
    assertNullShareValue(allInRange, norInRangeN1);
    assertNullShareValue(allInRange, notInRangeN4);
    assertShareValue(allInRange, inRangeN2, share2.getVolume());
    assertShareValue(allInRange, inRangeN3, share3.getVolume());
  }

  @Test
  void findTopVolumesInRange_success_sorted() {

    Optional<LocalDateTime> lastDateOp = daysJooqDao.getLastDate();
    LocalDateTime lastDate = lastDateOp.orElse(LocalDateTime.now().truncatedTo(DAYS));

    LocalDateTime day1Date = lastDate.plusDays(1);
    LocalDateTime day2Date = lastDate.plusDays(2);

    Day day1 = daysJooqDao.insert(day1Date, dsl.configuration());
    Day day2 = daysJooqDao.insert(day2Date, dsl.configuration());

    Share share1 = getShareNameAsTicker(day1, "N1", 300L);
    Share share2 = getShareNameAsTicker(day1, "N2", 100L);
    Share share3 = getShareNameAsTicker(day2, "N3", 500L);
    Share share4 = getShareNameAsTicker(day2, "N4", 200L);
    Share share5 = getShareNameAsTicker(day2, "N5", 400L);

    List<Share> shareList = List.of(share1, share2, share3, share4, share5);
    shareDao.insert(shareList);

    TimeRangeRequest timeRangeRequest = new TimeRangeRequest();
    timeRangeRequest.setStart(day1Date);
    timeRangeRequest.setEnd(day2Date);

    List<ShareValue> allInRange = sharesJooqDao.findTopVolumesInRange(timeRangeRequest);

    List<ShareValue> expectedList = shareList.stream()
        .map(share -> new ShareValue(share.getName(), BigDecimal.valueOf(share.getVolume())))
        .sorted(Comparator.comparing(ShareValue::getValue).reversed())
        .collect(Collectors.toList());

    assertEquals(expectedList, allInRange);
  }

  private Share getShareNameAsTicker(Day day, String name, long volume) {
    return getShare(day, name, name, volume);
  }

  private Share getShare(Day day, String name, String ticker, long volume) {
    Share share = new Share();
    share.setDay(day.getId());
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
    share.setVolume(volume);
    share.setTurnover(BigDecimal.TEN);
    share.setIndustry("Industry");
    share.setSupersector("Supersector");
    return share;
  }

  private void assertShare(Share shareDb, Share share) {
    assertShare(shareDb, share.getName(), share.getIsin(), share.getCurrency(), share.getMarketplace(), share.getList(),
        share.getAveragePrice(), share.getOpenPrice(), share.getHighPrice(), share.getLowPrice(), share.getLastClosePrice(),
        share.getLastPrice(), share.getPriceChangePercentage(), share.getBestBid(), share.getBestAsk(), share.getTrades(),
        share.getVolume(), share.getTurnover(), share.getIndustry(), share.getSupersector());
  }

  private void assertShare(final Share share, String name, String isin, Object currency, String marketplace, String list,
      BigDecimal averagePrice, BigDecimal openPrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal lastClosePrice,
      BigDecimal lastPrice, BigDecimal priceChangePercentage, BigDecimal bestBid, BigDecimal bestAsk, Integer trades,
      Long volume, BigDecimal turnover, String industry, String supersector) {

    assertEquals(name, share.getName());
    assertEquals(isin, share.getIsin());
    assertEquals(currency, share.getCurrency());
    assertEquals(marketplace, share.getMarketplace());
    assertEquals(list, share.getList());
    assertEquals(0, averagePrice.compareTo(share.getAveragePrice()));
    assertEquals(0, openPrice.compareTo(share.getOpenPrice()));
    assertEquals(0, highPrice.compareTo(share.getHighPrice()));
    assertEquals(0, lowPrice.compareTo(share.getLowPrice()));
    assertEquals(0, lastClosePrice.compareTo(share.getLastClosePrice()));
    assertEquals(0, lastPrice.compareTo(share.getLastPrice()));
    assertEquals(0, priceChangePercentage.compareTo(share.getPriceChangePercentage()));
    assertEquals(0, bestBid.compareTo(share.getBestBid()));
    assertEquals(0, bestAsk.compareTo(share.getBestAsk()));
    assertEquals(trades, share.getTrades());
    assertEquals(volume, share.getVolume());
    assertEquals(0, turnover.compareTo(share.getTurnover()));
    assertEquals(industry, share.getIndustry());
    assertEquals(supersector, share.getSupersector());
  }

  private void assertNullShareValue(final List<ShareValue> shares, final String name) {
    assertTrue(shares.stream().noneMatch(shareValue -> name.equals(shareValue.getName())));
  }

  private void assertShareValue(final List<ShareValue> shares, final String name, Long... values) {
    List<BigDecimal> bigDecimals = Arrays.stream(values).map(BigDecimal::valueOf).collect(Collectors.toList());
    assertShareValue(shares, name, bigDecimals);
  }

  private void assertShareValue(final List<ShareValue> shares, final String name, List<BigDecimal> values) {
    final BigDecimal total = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    assertTrue(shares.stream().anyMatch(shareValue -> shareValue.getName().equals(name) && shareValue.getValue().compareTo(total) == 0));
  }
}
