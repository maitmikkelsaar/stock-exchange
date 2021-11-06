package ee.mikkelsaar.stockapi.client;

import java.time.LocalDateTime;

public interface NasdaqClient {

  byte[] getXlsx(LocalDateTime localDateTime);
}
