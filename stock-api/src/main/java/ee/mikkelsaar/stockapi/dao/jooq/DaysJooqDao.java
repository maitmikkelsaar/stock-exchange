package ee.mikkelsaar.stockapi.dao.jooq;

import static ee.mikkelsaar.tables.Day.DAY;

import ee.mikkelsaar.tables.pojos.Day;
import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DaysJooqDao {

  private final DSLContext dsl;
  private final SharesJooqDao sharesJooqDao;

  public Day insert(final LocalDateTime dateTime, final Configuration configuration) {
    return DSL.using(configuration).insertInto(
        DAY,
        DAY.DATE)
        .values(dateTime)
        .returning()
        .fetchOne()
        .into(Day.class);
  }

  public Optional<LocalDateTime> getLastDate() {
    return dsl.select(DAY.DATE)
        .from(DAY)
        .orderBy(DAY.DATE.desc())
        .limit(1)
        .fetchOptionalInto(LocalDateTime.class);
  }

  public Day upsertDayWithShares(LocalDateTime dateTime, List<Share> shareList) {
    return dsl.transactionResult(configuration -> {

      Optional<Day> optionalDay = DSL.using(configuration)
          .selectFrom(DAY)
          .where(DAY.DATE.eq(dateTime))
          .fetchOptionalInto(Day.class);

      Day day = optionalDay.orElseGet(() -> insert(dateTime, configuration));
      shareList.forEach(share -> share.setDay(day.getId()));

      sharesJooqDao.upsert(shareList, configuration);
      return day;
    });
  }
}
