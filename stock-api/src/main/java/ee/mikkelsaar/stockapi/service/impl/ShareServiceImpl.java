package ee.mikkelsaar.stockapi.service.impl;

import ee.mikkelsaar.stockapi.client.NasdaqClient;
import ee.mikkelsaar.stockapi.service.ExcelParser;
import ee.mikkelsaar.stockapi.service.ShareService;
import ee.mikkelsaar.tables.pojos.Share;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShareServiceImpl implements ShareService {

  private final NasdaqClient nasdaqClient;
  private final ExcelParser excelParser;

  @Override
  public List<Share> fetchShares(final LocalDateTime toGet) {
    byte[] xlsx = nasdaqClient.getXlsx(toGet);
    return excelParser.parse(xlsx);
  }
}
