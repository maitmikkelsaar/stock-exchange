package ee.mikkelsaar.stockapi.service.impl;

import ee.mikkelsaar.stockapi.exception.ApiException;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExcelParserImpl implements ExcelParser {

  public static final String XSSFWORKBOOK_ERROR = "Could not read xlsx InputStream to XSSFWorkbook due to ";

  @Override
  public List<Share> parse(byte[] xlsx) {
    InputStream inputStream = new ByteArrayInputStream(xlsx);
    Workbook workbook;
    try {
      workbook = new XSSFWorkbook(inputStream);
    } catch (Exception e) {
      String message = XSSFWORKBOOK_ERROR + e;
      log.error(message);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
    Sheet datatypeSheet = workbook.getSheetAt(0);
    Iterator<Row> iterator = datatypeSheet.iterator();

    readSheetHeadings(iterator);

    return mapAllRowsToShares(iterator);
  }

  private void readSheetHeadings(Iterator<Row> iterator) {
    iterator.next();
  }

  private List<Share> mapAllRowsToShares(Iterator<Row> iterator) {
    List<Share> shares = new ArrayList<>();

    while (iterator.hasNext()) {
      Row currentRow = iterator.next();

      Share share = mapRowToShare(currentRow);
      shares.add(share);
    }
    return shares;
  }

  private Share mapRowToShare(Row currentRow) {
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
