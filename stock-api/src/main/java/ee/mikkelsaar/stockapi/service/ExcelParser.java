package ee.mikkelsaar.stockapi.service;

import ee.mikkelsaar.tables.pojos.Share;
import java.util.List;

public interface ExcelParser {

  List<Share> parse(byte[] xlsx);
}
