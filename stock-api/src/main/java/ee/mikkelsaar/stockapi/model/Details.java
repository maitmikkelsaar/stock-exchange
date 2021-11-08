package ee.mikkelsaar.stockapi.model;

import java.util.List;
import lombok.Data;

@Data
public class Details {
  private final GainersDecliners gainersDecliners;
  private final List<ShareValue> mostActive;
  private final List<ShareValue> biggestTurnover;
}

