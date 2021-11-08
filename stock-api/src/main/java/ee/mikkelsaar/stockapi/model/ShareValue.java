package ee.mikkelsaar.stockapi.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ShareValue {
  private final String name;
  private final BigDecimal value;
}