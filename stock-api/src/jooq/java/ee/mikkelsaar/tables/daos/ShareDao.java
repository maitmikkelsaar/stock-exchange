/*
 * This file is generated by jOOQ.
 */
package ee.mikkelsaar.tables.daos;


import ee.mikkelsaar.tables.Share;
import ee.mikkelsaar.tables.records.ShareRecord;

import java.math.BigDecimal;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ShareDao extends DAOImpl<ShareRecord, ee.mikkelsaar.tables.pojos.Share, Long> {

    /**
     * Create a new ShareDao without any configuration
     */
    public ShareDao() {
        super(Share.SHARE, ee.mikkelsaar.tables.pojos.Share.class);
    }

    /**
     * Create a new ShareDao with an attached configuration
     */
    public ShareDao(Configuration configuration) {
        super(Share.SHARE, ee.mikkelsaar.tables.pojos.Share.class, configuration);
    }

    @Override
    public Long getId(ee.mikkelsaar.tables.pojos.Share object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchById(Long... values) {
        return fetch(Share.SHARE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public ee.mikkelsaar.tables.pojos.Share fetchOneById(Long value) {
        return fetchOne(Share.SHARE.ID, value);
    }

    /**
     * Fetch records that have <code>day BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfDay(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.DAY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>day IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByDay(Long... values) {
        return fetch(Share.SHARE.DAY, values);
    }

    /**
     * Fetch records that have <code>ticker BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfTicker(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.TICKER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ticker IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByTicker(String... values) {
        return fetch(Share.SHARE.TICKER, values);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByName(String... values) {
        return fetch(Share.SHARE.NAME, values);
    }

    /**
     * Fetch records that have <code>isin BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfIsin(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.ISIN, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>isin IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByIsin(String... values) {
        return fetch(Share.SHARE.ISIN, values);
    }

    /**
     * Fetch records that have <code>currency BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfCurrency(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.CURRENCY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>currency IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByCurrency(String... values) {
        return fetch(Share.SHARE.CURRENCY, values);
    }

    /**
     * Fetch records that have <code>marketplace BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfMarketplace(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.MARKETPLACE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>marketplace IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByMarketplace(String... values) {
        return fetch(Share.SHARE.MARKETPLACE, values);
    }

    /**
     * Fetch records that have <code>list BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfList(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.LIST, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>list IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByList(String... values) {
        return fetch(Share.SHARE.LIST, values);
    }

    /**
     * Fetch records that have <code>average_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfAveragePrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.AVERAGE_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>average_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByAveragePrice(BigDecimal... values) {
        return fetch(Share.SHARE.AVERAGE_PRICE, values);
    }

    /**
     * Fetch records that have <code>open_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfOpenPrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.OPEN_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>open_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByOpenPrice(BigDecimal... values) {
        return fetch(Share.SHARE.OPEN_PRICE, values);
    }

    /**
     * Fetch records that have <code>high_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfHighPrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.HIGH_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>high_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByHighPrice(BigDecimal... values) {
        return fetch(Share.SHARE.HIGH_PRICE, values);
    }

    /**
     * Fetch records that have <code>low_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfLowPrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.LOW_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>low_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByLowPrice(BigDecimal... values) {
        return fetch(Share.SHARE.LOW_PRICE, values);
    }

    /**
     * Fetch records that have <code>last_close_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfLastClosePrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.LAST_CLOSE_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>last_close_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByLastClosePrice(BigDecimal... values) {
        return fetch(Share.SHARE.LAST_CLOSE_PRICE, values);
    }

    /**
     * Fetch records that have <code>last_price BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfLastPrice(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.LAST_PRICE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>last_price IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByLastPrice(BigDecimal... values) {
        return fetch(Share.SHARE.LAST_PRICE, values);
    }

    /**
     * Fetch records that have <code>price_change_percentage BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfPriceChangePercentage(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.PRICE_CHANGE_PERCENTAGE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>price_change_percentage IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByPriceChangePercentage(BigDecimal... values) {
        return fetch(Share.SHARE.PRICE_CHANGE_PERCENTAGE, values);
    }

    /**
     * Fetch records that have <code>best_bid BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfBestBid(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.BEST_BID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>best_bid IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByBestBid(BigDecimal... values) {
        return fetch(Share.SHARE.BEST_BID, values);
    }

    /**
     * Fetch records that have <code>best_ask BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfBestAsk(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.BEST_ASK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>best_ask IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByBestAsk(BigDecimal... values) {
        return fetch(Share.SHARE.BEST_ASK, values);
    }

    /**
     * Fetch records that have <code>trades BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfTrades(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Share.SHARE.TRADES, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>trades IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByTrades(Integer... values) {
        return fetch(Share.SHARE.TRADES, values);
    }

    /**
     * Fetch records that have <code>volume BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfVolume(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Share.SHARE.VOLUME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>volume IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByVolume(Long... values) {
        return fetch(Share.SHARE.VOLUME, values);
    }

    /**
     * Fetch records that have <code>turnover BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfTurnover(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Share.SHARE.TURNOVER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>turnover IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByTurnover(BigDecimal... values) {
        return fetch(Share.SHARE.TURNOVER, values);
    }

    /**
     * Fetch records that have <code>industry BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfIndustry(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.INDUSTRY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>industry IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchByIndustry(String... values) {
        return fetch(Share.SHARE.INDUSTRY, values);
    }

    /**
     * Fetch records that have <code>supersector BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchRangeOfSupersector(String lowerInclusive, String upperInclusive) {
        return fetchRange(Share.SHARE.SUPERSECTOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>supersector IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Share> fetchBySupersector(String... values) {
        return fetch(Share.SHARE.SUPERSECTOR, values);
    }
}
