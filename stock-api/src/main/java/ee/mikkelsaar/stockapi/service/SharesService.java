package ee.mikkelsaar.stockapi.service;

import ee.mikkelsaar.stockapi.model.GainersDecliners;
import ee.mikkelsaar.stockapi.model.ShareValue;
import ee.mikkelsaar.stockapi.model.TimeRangeRequest;
import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;

public interface SharesService {

  List<Share> fetchShares(LocalDateTime toGet);

  List<Share> getAllByDay(Long dayId);

  GainersDecliners getGainersDecliners(List<Share> allByDay);

  List<ShareValue> getMostActive(List<Share> allByDay);

  List<ShareValue> getBiggestTurnover(List<Share> allByDay);

  List<ShareValue> getTimeRangeVolumes(TimeRangeRequest timeRangeRequest);
}
