package ee.mikkelsaar.stockapi.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Details {
  private GainersDecliners gainersDecliners;
  private List<ShareValue> mostActive;
  private List<ShareValue> biggestTurnover;
}

