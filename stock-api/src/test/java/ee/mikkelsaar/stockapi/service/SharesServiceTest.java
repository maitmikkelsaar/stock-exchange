package ee.mikkelsaar.stockapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.service.impl.SharesServiceImpl;
import ee.mikkelsaar.tables.pojos.Share;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class SharesServiceTest {

  @Test
  void getGainersDecliners() {

    SharesService sharesService = new SharesServiceImpl(null, null, null, null);

    Share share1 = getShareWithPriceChangePercentage("share1", new BigDecimal("3"));
    Share share2 = getShareWithPriceChangePercentage("share2", new BigDecimal("-2"));
    Share share3 = getShareWithPriceChangePercentage("share3", new BigDecimal("5"));
    Share share4 = getShareWithPriceChangePercentage("share4", new BigDecimal("-4"));
    Share share5 = getShareWithPriceChangePercentage("share5", new BigDecimal("-5"));
    Share share6 = getShareWithPriceChangePercentage("share6", new BigDecimal("2"));
    Share share7 = getShareWithPriceChangePercentage("share7", new BigDecimal("4"));
    Share share8 = getShareWithPriceChangePercentage("share8", new BigDecimal("-3"));

    List<Share> shares = List.of(share1, share2, share3, share4, share5, share6, share7, share8);

    GainersDecliners gainersDecliners = sharesService.getGainersDecliners(shares);

    assertShareValue(gainersDecliners.getGainers().get(0), share3.getName(), share3.getPriceChangePercentage());
    assertShareValue(gainersDecliners.getGainers().get(1), share7.getName(), share7.getPriceChangePercentage());
    assertShareValue(gainersDecliners.getGainers().get(2), share1.getName(), share1.getPriceChangePercentage());
    assertShareValue(gainersDecliners.getDecliners().get(0), share8.getName(), share8.getPriceChangePercentage());
    assertShareValue(gainersDecliners.getDecliners().get(1), share4.getName(), share4.getPriceChangePercentage());
    assertShareValue(gainersDecliners.getDecliners().get(2), share5.getName(), share5.getPriceChangePercentage());
  }

  @Test
  public void mostActive() {

    SharesService sharesService = new SharesServiceImpl(null, null, null, null);

    Share share1 = getShareWithTrades("share1", 20);
    Share share2 = getShareWithTrades("share2", 10);
    Share share3 = getShareWithTrades("share3", 40);
    Share share4 = getShareWithTrades("share4", 30);
    Share share5 = getShareWithTrades("share5", 60);
    Share share6 = getShareWithTrades("share6", 0);
    Share share7 = getShareWithTrades("share7", 50);

    List<Share> shares = List.of(share1, share2, share3, share4, share5, share6, share7);
    List<ShareValue> shareValues = sharesService.getMostActive(shares);

    assertEquals(6, shareValues.size());
    assertShareValue(shareValues.get(0), share5.getName(), BigDecimal.valueOf(share5.getTrades()));
    assertShareValue(shareValues.get(1), share7.getName(), BigDecimal.valueOf(share7.getTrades()));
    assertShareValue(shareValues.get(2), share3.getName(), BigDecimal.valueOf(share3.getTrades()));
    assertShareValue(shareValues.get(3), share4.getName(), BigDecimal.valueOf(share4.getTrades()));
    assertShareValue(shareValues.get(4), share1.getName(), BigDecimal.valueOf(share1.getTrades()));
    assertShareValue(shareValues.get(5), share2.getName(), BigDecimal.valueOf(share2.getTrades()));
  }

  @Test
  public void biggestTurnover() {

    SharesService sharesService = new SharesServiceImpl(null, null, null, null);

    Share share1 = getShareWithTurnover("share1", new BigDecimal("100"));
    Share share2 = getShareWithTurnover("share2", new BigDecimal("600"));
    Share share3 = getShareWithTurnover("share3", new BigDecimal("200"));
    Share share4 = getShareWithTurnover("share4", new BigDecimal("500"));
    Share share5 = getShareWithTurnover("share5", new BigDecimal("400"));
    Share share6 = getShareWithTurnover("share6", new BigDecimal("300"));
    Share share7 = getShareWithTurnover("share7", new BigDecimal("0"));

    List<Share> shares = List.of(share1, share2, share3, share4, share5, share6, share7);
    List<ShareValue> shareValues = sharesService.getBiggestTurnover(shares);

    assertEquals(6, shareValues.size());
    assertShareValue(shareValues.get(0), share2.getName(), share2.getTurnover());
    assertShareValue(shareValues.get(1), share4.getName(), share4.getTurnover());
    assertShareValue(shareValues.get(2), share5.getName(), share5.getTurnover());
    assertShareValue(shareValues.get(3), share6.getName(), share6.getTurnover());
    assertShareValue(shareValues.get(4), share3.getName(), share3.getTurnover());
    assertShareValue(shareValues.get(5), share1.getName(), share1.getTurnover());
  }

  private Share getShareWithPriceChangePercentage(final String name, final BigDecimal priceChangePercentage) {
    return getShare(name, priceChangePercentage, null, null);
  }

  private Share getShareWithTrades(final String name, final Integer trades) {
    return getShare(name, null, trades, null);
  }

  private Share getShareWithTurnover(final String name, final BigDecimal turnover) {
    return getShare(name, null, null, turnover);
  }

  private Share getShare(final String name, final BigDecimal priceChangePercentage, final Integer trades, final BigDecimal turnover) {
    Share share = new Share();
    share.setName(name);
    share.setPriceChangePercentage(priceChangePercentage);
    share.setTrades(trades);
    share.setTurnover(turnover);
    return share;
  }

  private void assertShareValue(ShareValue shareValue, String name, BigDecimal priceChangePercentage) {
    assertEquals(name, shareValue.getName());
    assertEquals(priceChangePercentage, shareValue.getValue());
  }
}