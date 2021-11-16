package ee.mikkelsaar.stockapi.service;

import static ee.mikkelsaar.stockapi.service.impl.ExcelParserImpl.XSSFWORKBOOK_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ee.mikkelsaar.stockapi.exception.ApiException;
import ee.mikkelsaar.stockapi.service.impl.ExcelParserImpl;
import ee.mikkelsaar.tables.pojos.Share;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class ExcelParserTest {

  public static final String FILES_SHARES_XLSX = "files/shares.xlsx";
  public static final String FILES_SHARES_EMPTY_XLSX = "files/shares_empty.xlsx";
  public static final String BAD_STREAM = "Bad stream";

  @Test
  void parse_success_emptyByteArray() {
    ExcelParser excelParser = new ExcelParserImpl();

    byte[] file = {};
    Exception exception = assertThrows(Exception.class, () -> excelParser.parse(file));
    assertTrue(exception.getMessage().startsWith(XSSFWORKBOOK_ERROR));
  }

  @Test
  void parse_success_notValidOOXML() {
    ExcelParser excelParser = new ExcelParserImpl();

    byte[] file = BAD_STREAM.getBytes();
    ApiException exception = assertThrows(ApiException.class, () -> excelParser.parse(file));
    assertTrue(exception.getMessage().startsWith(XSSFWORKBOOK_ERROR));
  }

  @Test
  void parse_success_withAllFields() throws IOException {
    ExcelParser excelParser = new ExcelParserImpl();

    byte[] file = this.getClass().getClassLoader().getResourceAsStream(FILES_SHARES_XLSX).readAllBytes();
    List<Share> shareList = excelParser.parse(file);

    assertEquals(13, shareList.size());

    String allDataShareTicker = "GRG1L";
    Share allDataShare = shareList.stream()
        .filter(share -> allDataShareTicker.equals(share.getTicker()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(allDataShareTicker + "was not parsed from xlsx file"));

    assertShare(
        allDataShare,
        "Grigeo",
        "LT0000102030",
        "EUR",
        "VLN",
        "Baltic Main List",
        new BigDecimal("0.98744055"),
        new BigDecimal("0.988"),
        new BigDecimal("0.99"),
        new BigDecimal("0.984"),
        new BigDecimal("0.986"),
        new BigDecimal("0.99"),
        new BigDecimal("0.41"),
        new BigDecimal("0.986"),
        new BigDecimal("0.99"),
        38,
        34763L,
        new BigDecimal("34326.396"),
        "Basic Materials",
        "Basic Resources");
  }

  @Test
  void parse_success_withNullValues() throws IOException {
    ExcelParser excelParser = new ExcelParserImpl();

    byte[] file = this.getClass().getClassLoader().getResourceAsStream(FILES_SHARES_XLSX).readAllBytes();
    List<Share> shareList = excelParser.parse(file);

    assertEquals(13, shareList.size());

    assertShareNullField(shareList, "AMG1L", Share::getAveragePrice);
    assertShareNullField(shareList, "APG1L", Share::getOpenPrice);
    assertShareNullField(shareList, "ARC1T", Share::getHighPrice);
    assertShareNullField(shareList, "AUG1L", Share::getLowPrice);
    assertShareNullField(shareList, "BAL1R", Share::getLastClosePrice);
    assertShareNullField(shareList, "BERCM", Share::getLastPrice);
    assertShareNullField(shareList, "BLT1T", Share::getPriceChangePercentage);
    assertShareNullField(shareList, "BTE1R", Share::getBestBid);
    assertShareNullField(shareList, "CPA1T", Share::getBestAsk);
    assertShareNullField(shareList, "DPK1R", Share::getTrades);
    assertShareNullField(shareList, "EEG1T", Share::getVolume);
    assertShareNullField(shareList, "EFT1T", Share::getTurnover);
  }

  @Test
  void parse_success_emptyFile() throws IOException {
    ExcelParser excelParser = new ExcelParserImpl();

    byte[] file = this.getClass().getClassLoader().getResourceAsStream(FILES_SHARES_EMPTY_XLSX).readAllBytes();
    List<Share> shareList = excelParser.parse(file);

    assertTrue(shareList.isEmpty());
  }

  private void assertShare(final Share share, String name, String isin, Object currency, String marketplace, String list,
      BigDecimal averagePrice, BigDecimal openPrice, BigDecimal highPrice, BigDecimal lowPrice, BigDecimal lastClosePrice,
      BigDecimal lastPrice, BigDecimal priceChangePercentage, BigDecimal bestBid, BigDecimal bestAsk, Integer trades,
      Long volume, BigDecimal turnover, String industry, String supersector) {

    assertEquals(share.getName(), name);
    assertEquals(share.getIsin(), isin);
    assertEquals(share.getCurrency(), currency);
    assertEquals(share.getMarketplace(), marketplace);
    assertEquals(share.getList(), list);
    assertEquals(share.getAveragePrice(), averagePrice);
    assertEquals(share.getOpenPrice(), openPrice);
    assertEquals(share.getHighPrice(), highPrice);
    assertEquals(share.getLowPrice(), lowPrice);
    assertEquals(share.getLastClosePrice(), lastClosePrice);
    assertEquals(share.getLastPrice(), lastPrice);
    assertEquals(share.getPriceChangePercentage(), priceChangePercentage);
    assertEquals(share.getBestBid(), bestBid);
    assertEquals(share.getBestAsk(), bestAsk);
    assertEquals(share.getTrades(), trades);
    assertEquals(share.getVolume(), volume);
    assertEquals(share.getTurnover(), turnover);
    assertEquals(share.getIndustry(), industry);
    assertEquals(share.getSupersector(), supersector);
  }

  private void assertShareNullField(final List<Share> shareList, final String shareTicker, final Function<Share, Object> function) {
    assertTrue(shareList.stream().anyMatch(share -> shareTicker.equals(share.getTicker()) && Objects.isNull(function.apply(share))));
  }
}