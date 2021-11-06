package ee.mikkelsaar.stockapi.service;

import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;

public interface ShareService {

  List<Share> fetchShares(LocalDateTime toGet);
}
