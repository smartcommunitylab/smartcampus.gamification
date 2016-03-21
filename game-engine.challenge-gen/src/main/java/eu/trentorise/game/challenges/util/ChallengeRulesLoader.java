package eu.trentorise.game.challenges.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Load challenges rule from and excel file
 *
 * {@link IOUtils}, {@link HSSFWorkbook}
 */
public final class ChallengeRulesLoader {

    private static final Logger logger = LogManager
	    .getLogger(ChallengeRulesLoader.class);

    private ChallengeRulesLoader() {
    }

    public static ChallengeRules load(String ref) throws IOException,
	    NullPointerException, IllegalArgumentException {
	if (ref == null) {
	    logger.error("Input file must be not null");
	    throw new NullPointerException("Input file must be not null");
	}
	HSSFWorkbook workbook = null;
	try {
	    // open excel file
	    InputStream inputStream = new ByteArrayInputStream(
		    IOUtils.toByteArray(Thread.currentThread()
			    .getContextClassLoader().getResourceAsStream(ref)));

	    workbook = new HSSFWorkbook(inputStream);
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    if (sheet == null) {
		logger.error("File with name " + ref + " not found");
		throw new IllegalArgumentException("File with name " + ref
			+ " not found");
	    }

	    // create response
	    ChallengeRules response = new ChallengeRules();
	    Iterator<Row> iter = sheet.iterator();
	    iter.next(); // avoid first row
	    Row row;
	    while (iter.hasNext()) {
		row = iter.next();
		ChallengeRuleRow crr = new ChallengeRuleRow();
		crr.setName(row.getCell(0).getStringCellValue());
		crr.setType(row.getCell(1).getStringCellValue());
		crr.setGoalType(row.getCell(2).getStringCellValue());
		Cell tc = row.getCell(3);
		if (tc.getCellType() == Cell.CELL_TYPE_NUMERIC) {
		    crr.setTarget(new Double(tc.getNumericCellValue()));
		} else if (tc.getCellType() == Cell.CELL_TYPE_STRING) {
		    crr.setTarget(tc.getStringCellValue());
		}
		Integer bonus = (int) Math.round(row.getCell(4)
			.getNumericCellValue());
		crr.setBonus(bonus);
		crr.setPointType(row.getCell(5).getStringCellValue());
		crr.setSelectionCriteria(row.getCell(7).getStringCellValue());
		response.getChallenges().add(crr);
	    }
	    logger.debug("Rows in excel file "
		    + response.getChallenges().size());
	    return response;
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    if (workbook != null) {
		workbook.close();
	    }
	}
    }
}
