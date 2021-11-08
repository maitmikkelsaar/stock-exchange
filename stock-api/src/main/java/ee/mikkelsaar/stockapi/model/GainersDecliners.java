package ee.mikkelsaar.stockapi.model;

import java.util.List;
import lombok.Data;

@Data
public class GainersDecliners {
  private final List<ShareValue> gainers;
  private final List<ShareValue> decliners;
}
