package ee.mikkelsaar.stockapi.service.impl;

import ee.mikkelsaar.stockapi.client.NasdaqClient;
import ee.mikkelsaar.stockapi.dao.jooq.SharesJooqDao;
import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.stockapi.service.ExcelParser;
import ee.mikkelsaar.stockapi.service.SharesService;
import ee.mikkelsaar.tables.daos.ShareDao;
import ee.mikkelsaar.tables.pojos.Share;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SharesServiceImpl implements SharesService {

  private final NasdaqClient nasdaqClient;
  private final ExcelParser excelParser;
  private final ShareDao shareDao;
  private final SharesJooqDao sharesJooqDao;

  @Override
  public List<Share> fetchShares(final LocalDateTime toGet) {
    byte[] xlsx = nasdaqClient.getXlsx(toGet);
    return excelParser.parse(xlsx);
  }

  @Override
  public List<Share> getAllByDay(final Long dayId) {
    return shareDao.fetchByDay(dayId);
  }

  @Override
  public GainersDecliners getGainersDecliners(final List<Share> allByDay) {
    List<Share> shares = allByDay.stream()
        .filter(share -> Objects.nonNull(share.getPriceChangePercentage()))
        .sorted(Comparator.comparing(Share::getPriceChangePercentage))
        .collect(Collectors.toList());

    List<ShareValue> decliners = getDecliners(shares);
    List<ShareValue> gainers = getGainers(shares);

    return new GainersDecliners(gainers, decliners);
  }

  @Override
  public List<ShareValue> getMostActive(final List<Share> allByDay) {
    return allByDay.stream()
        .filter(share -> Objects.nonNull(share.getTrades()))
        .sorted(Comparator.comparingLong(Share::getTrades).reversed())
        .limit(6L)
        .map(share -> new ShareValue(share.getName(), BigDecimal.valueOf(share.getTrades())))
        .collect(Collectors.toList());
  }

  @Override
  public List<ShareValue> getBiggestTurnover(final List<Share> allByDay) {
    return allByDay.stream()
        .filter(share -> Objects.nonNull(share.getTurnover()))
        .sorted(Comparator.comparing(Share::getTurnover).reversed())
        .limit(6L)
        .map(share -> new ShareValue(share.getName(), share.getTurnover()))
        .collect(Collectors.toList());
  }

  @Override
  public List<ShareValue> getTimeRangeVolumes(TimeRangeRequest timeRangeRequest) {
    return sharesJooqDao.findTopVolumesInRange(timeRangeRequest);
  }

  private List<ShareValue> getGainers(final List<Share> shares) {
    Collections.reverse(shares);
    return shares.stream()
        .limit(3)
        .map(share -> new ShareValue(share.getName(), share.getPriceChangePercentage()))
        .collect(Collectors.toList());
  }

  private List<ShareValue> getDecliners(final List<Share> shares) {
    return shares.stream()
        .limit(3)
        .sorted(Comparator.comparing(Share::getPriceChangePercentage).reversed())
        .map(share -> new ShareValue(share.getName(), share.getPriceChangePercentage()))
        .collect(Collectors.toList());
  }
}
