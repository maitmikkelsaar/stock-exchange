package ee.mikkelsaar.stockapi.dao.jooq;

import static ee.mikkelsaar.Keys.SHARE_DAY_TICKER_KEY;
import static ee.mikkelsaar.tables.Day.DAY;
import static ee.mikkelsaar.tables.Share.SHARE;
import static org.jooq.impl.DSL.sum;

import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.tables.pojos.Share;
import ee.mikkelsaar.tables.records.ShareRecord;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SharesJooqDao {

  private final DSLContext dsl;

  public Share insert(final Share share) {
    return dsl.insertInto(
        SHARE,
        SHARE.DAY,
        SHARE.TICKER,
        SHARE.NAME,
        SHARE.ISIN,
        SHARE.CURRENCY,
        SHARE.MARKETPLACE,
        SHARE.LIST,
        SHARE.AVERAGE_PRICE,
        SHARE.OPEN_PRICE,
        SHARE.HIGH_PRICE,
        SHARE.LOW_PRICE,
        SHARE.LAST_CLOSE_PRICE,
        SHARE.LAST_PRICE,
        SHARE.PRICE_CHANGE_PERCENTAGE,
        SHARE.BEST_BID,
        SHARE.BEST_ASK,
        SHARE.TRADES,
        SHARE.VOLUME,
        SHARE.TURNOVER,
        SHARE.INDUSTRY,
        SHARE.SUPERSECTOR)
        .values(
            share.getDay(),
            share.getTicker(),
            share.getName(),
            share.getIsin(),
            share.getCurrency(),
            share.getMarketplace(),
            share.getList(),
            share.getAveragePrice(),
            share.getOpenPrice(),
            share.getHighPrice(),
            share.getLowPrice(),
            share.getLastClosePrice(),
            share.getLastPrice(),
            share.getPriceChangePercentage(),
            share.getBestBid(),
            share.getBestAsk(),
            share.getTrades(),
            share.getVolume(),
            share.getTurnover(),
            share.getIndustry(),
            share.getSupersector())
        .returning()
        .fetchOptional()
        .orElseThrow(() -> new RuntimeException("Unable to get inserted Share"))
        .into(Share.class);
  }

  public void upsert(List<Share> shares, Configuration configuration) {
    List<InsertOnDuplicateSetMoreStep<ShareRecord>> steps = shares.stream().map(share ->
        DSL.using(configuration).insertInto(
            SHARE,
            SHARE.DAY,
            SHARE.TICKER,
            SHARE.NAME,
            SHARE.ISIN,
            SHARE.CURRENCY,
            SHARE.MARKETPLACE,
            SHARE.LIST,
            SHARE.AVERAGE_PRICE,
            SHARE.OPEN_PRICE,
            SHARE.HIGH_PRICE,
            SHARE.LOW_PRICE,
            SHARE.LAST_CLOSE_PRICE,
            SHARE.LAST_PRICE,
            SHARE.PRICE_CHANGE_PERCENTAGE,
            SHARE.BEST_BID,
            SHARE.BEST_ASK,
            SHARE.TRADES,
            SHARE.VOLUME,
            SHARE.TURNOVER,
            SHARE.INDUSTRY,
            SHARE.SUPERSECTOR)
            .values(
                share.getDay(),
                share.getTicker(),
                share.getName(),
                share.getIsin(),
                share.getCurrency(),
                share.getMarketplace(),
                share.getList(),
                share.getAveragePrice(),
                share.getOpenPrice(),
                share.getHighPrice(),
                share.getLowPrice(),
                share.getLastClosePrice(),
                share.getLastPrice(),
                share.getPriceChangePercentage(),
                share.getBestBid(),
                share.getBestAsk(),
                share.getTrades(),
                share.getVolume(),
                share.getTurnover(),
                share.getIndustry(),
                share.getSupersector())
            .onConflictOnConstraint(SHARE_DAY_TICKER_KEY)
            .doUpdate()
            .set(SHARE.AVERAGE_PRICE, share.getAveragePrice())
            .set(SHARE.OPEN_PRICE, share.getOpenPrice())
            .set(SHARE.HIGH_PRICE, share.getHighPrice())
            .set(SHARE.LOW_PRICE, share.getLowPrice())
            .set(SHARE.LAST_CLOSE_PRICE, share.getLastClosePrice())
            .set(SHARE.LAST_PRICE, share.getLastPrice())
            .set(SHARE.PRICE_CHANGE_PERCENTAGE, share.getPriceChangePercentage())
            .set(SHARE.BEST_BID, share.getBestBid())
            .set(SHARE.BEST_ASK, share.getBestAsk())
            .set(SHARE.TRADES, share.getTrades())
            .set(SHARE.VOLUME, share.getVolume())
            .set(SHARE.TURNOVER, share.getTurnover()))
        .collect(Collectors.toList());

    DSL.using(configuration).batch(steps).execute();
  }

  public List<ShareValue> findTopVolumesInRange(TimeRangeRequest timeRangeRequest) {
    Field<BigDecimal> value = sum(SHARE.VOLUME).as("value");
    return dsl.select(SHARE.NAME, value)
        .from(DAY)
        .join(SHARE)
        .on(DAY.ID.eq(SHARE.DAY))
        .where(DAY.DATE.between(timeRangeRequest.getStart(), timeRangeRequest.getEnd()))
        .and(SHARE.VOLUME.isNotNull())
        .groupBy(SHARE.NAME)
        .orderBy(value.desc())
        .limit(10)
        .fetchInto(ShareValue.class);
  }

}
