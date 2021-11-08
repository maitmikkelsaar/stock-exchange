package ee.mikkelsaar.stockapi.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TimeRangeRequest {
  private LocalDateTime start;
  private LocalDateTime end;
}
