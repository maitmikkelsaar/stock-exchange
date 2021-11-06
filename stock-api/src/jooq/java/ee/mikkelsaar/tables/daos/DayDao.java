/*
 * This file is generated by jOOQ.
 */
package ee.mikkelsaar.tables.daos;


import ee.mikkelsaar.tables.Day;
import ee.mikkelsaar.tables.records.DayRecord;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DayDao extends DAOImpl<DayRecord, ee.mikkelsaar.tables.pojos.Day, Long> {

    /**
     * Create a new DayDao without any configuration
     */
    public DayDao() {
        super(Day.DAY, ee.mikkelsaar.tables.pojos.Day.class);
    }

    /**
     * Create a new DayDao with an attached configuration
     */
    public DayDao(Configuration configuration) {
        super(Day.DAY, ee.mikkelsaar.tables.pojos.Day.class, configuration);
    }

    @Override
    public Long getId(ee.mikkelsaar.tables.pojos.Day object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Day> fetchRangeOfId(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Day.DAY.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Day> fetchById(Long... values) {
        return fetch(Day.DAY.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public ee.mikkelsaar.tables.pojos.Day fetchOneById(Long value) {
        return fetchOne(Day.DAY.ID, value);
    }

    /**
     * Fetch records that have <code>date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Day> fetchRangeOfDate(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Day.DAY.DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>date IN (values)</code>
     */
    public List<ee.mikkelsaar.tables.pojos.Day> fetchByDate(LocalDateTime... values) {
        return fetch(Day.DAY.DATE, values);
    }

    /**
     * Fetch a unique record that has <code>date = value</code>
     */
    public ee.mikkelsaar.tables.pojos.Day fetchOneByDate(LocalDateTime value) {
        return fetchOne(Day.DAY.DATE, value);
    }
}
