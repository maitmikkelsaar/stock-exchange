package ee.mikkelsaar.stockapi.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShareValue {
  private String name;
  private BigDecimal value;
}