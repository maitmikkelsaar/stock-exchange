package ee.mikkelsaar.stockapi.dao;

import ee.mikkelsaar.tables.daos.DayDao;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JooqDaoConfig {

  private final DSLContext dslContext;

  @Bean
  public DayDao dayDao() {
    return new DayDao(dslContext.configuration());
  }
}
