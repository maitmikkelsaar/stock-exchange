package ee.mikkelsaar.stockapi.service.impl;

import ee.mikkelsaar.stockapi.service.ExcelParser;
import ee.mikkelsaar.tables.pojos.Share;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelParserImpl implements ExcelParser {

  @Override
  public List<Share> parse(byte[] xlsx) {
    InputStream outputStream = new ByteArrayInputStream(xlsx);
    Workbook workbook;
    try {
      workbook = new XSSFWorkbook(outputStream);
    } catch (IOException e) {
      // TODO - Mait Mikkelsaar - 25 Oct 2021 - handle exception
      throw new IllegalArgumentException("Unable to get workbook");
    }
    Sheet datatypeSheet = workbook.getSheetAt(0);
    Iterator<Row> iterator = datatypeSheet.iterator();

    // TODO - Mait Mikkelsaar - 25 Oct 2021 - read first heading line
    iterator.next();

    List<Share> shares = new ArrayList<>();

    while (iterator.hasNext()) {
      Row currentRow = iterator.next();

      Share share = mapRowToStock(currentRow);
      shares.add(share);
    }

    return shares;
  }

  private Share mapRowToStock(Row currentRow) {
    Share share = new Share();
    share.setTicker(currentRow.getCell(0).getStringCellValue());
    share.setName(currentRow.getCell(1).getStringCellValue());
    share.setIsin(currentRow.getCell(2).getStringCellValue());
    share.setCurrency(currentRow.getCell(3).getStringCellValue());
    share.setMarketplace(currentRow.getCell(4).getStringCellValue());
    share.setList(currentRow.getCell(5).getStringCellValue());
    share.setAveragePrice(getBigDecimalOptionalValue(currentRow, 6));
    share.setOpenPrice(getBigDecimalOptionalValue(currentRow, 7));
    share.setHighPrice(getBigDecimalOptionalValue(currentRow, 8));
    share.setLowPrice(getBigDecimalOptionalValue(currentRow, 9));
    share.setLastClosePrice(getBigDecimalOptionalValue(currentRow, 10));
    share.setLastPrice(getBigDecimalOptionalValue(currentRow, 11));
    share.setPriceChangePercentage(getBigDecimalOptionalValue(currentRow, 12));
    share.setBestBid(getBigDecimalOptionalValue(currentRow, 13));
    share.setBestAsk(getBigDecimalOptionalValue(currentRow, 14));
    share.setTrades(getIntegerOptionalValue(currentRow, 15));
    share.setVolume(getLongOptionalValue(currentRow, 16));
    share.setTurnover(getBigDecimalOptionalValue(currentRow, 17));
    share.setIndustry(currentRow.getCell(18).getStringCellValue());
    share.setSupersector(currentRow.getCell(19).getStringCellValue());
    return share;
  }

  private BigDecimal getBigDecimalOptionalValue(final Row currentRow, final int index) {
    Cell cellValue = currentRow.getCell(index, MissingCellPolicy.RETURN_BLANK_AS_NULL);
    return Objects.isNull(cellValue) ? null : BigDecimal.valueOf(cellValue.getNumericCellValue());
  }

  private Integer getIntegerOptionalValue(final Row currentRow, final int index) {
    Cell cellValue = currentRow.getCell(index, MissingCellPolicy.RETURN_BLANK_AS_NULL);
    return Objects.isNull(cellValue) ? null : ((Double) cellValue.getNumericCellValue()).intValue();
  }

  private Long getLongOptionalValue(final Row currentRow, final int index) {
    Cell cellValue = currentRow.getCell(index, MissingCellPolicy.RETURN_BLANK_AS_NULL);
    return Objects.isNull(cellValue) ? null : ((Double) cellValue.getNumericCellValue()).longValue();
  }
}
