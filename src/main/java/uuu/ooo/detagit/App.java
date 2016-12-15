package uuu.ooo.detagit;

import static uuu.ooo.detagit.CellAttributes.ABBR_TEXT;
import static uuu.ooo.detagit.CellAttributes.ABBR_TITLE;
import static uuu.ooo.detagit.CellAttributes.A_HREF;
import static uuu.ooo.detagit.CellAttributes.ID;
import static uuu.ooo.detagit.CellAttributes.INPUT_NAME;
import static uuu.ooo.detagit.CellAttributes.INPUT_TYPE;
import static uuu.ooo.detagit.CellAttributes.INPUT_VAL;
import static uuu.ooo.detagit.CellAttributes.RESTRRICTED;
import static uuu.ooo.detagit.CellAttributes.TEXT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * CMD ARGS dir path, filename, table id
 * @author mstellert
 *
 */

public class App {
	private static final Logger LOGGER = LogManager.getLogger(App.class);
	private static Gson gson;
	private static Row headers = new Row();
	private static List<Row> rows = new ArrayList<>();
	
	private static String dir;
	private static String filename;
	private static String tableId;
	private static String outputFile;
	
	static {
		gson = new GsonBuilder().setPrettyPrinting()
								.disableHtmlEscaping()
								.create();
	}
	
	public static void main(String[] args) {
		dir = args[0];       
		filename = args[0] + "/" + args[1];
		tableId = args[2];
		outputFile = "/" + tableId.substring(1) + ".json";
		
		Document doc;
		try {
			doc = Jsoup.parse(new File(filename), "UTF-8");
			Elements table = doc.select(tableId);
			Elements tableHeader = table.select("thead tr th");
			tableHeader.forEach(th -> extractCellProps(th, headers));
			
			Elements tableRows = table.select("tbody tr");
			tableRows.forEach(tr -> fillRow(tr));
			
			rows.add(0, headers);
			
			String gsonString = gson.toJson(rows);
			LOGGER.info(gsonString);
			
			File output = new File(dir + outputFile);
			FileUtils.writeStringToFile(output, gsonString, "UTF-8", false);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	private static void fillRow(Element tr) {
		Row row = new Row();
		tr.select("td").forEach(td -> extractCellProps(td, row));
		rows.add(row);
	}

	private static void extractCellProps(Element tableCell, Row row) {
		Cell cell = new Cell();
		cell.addProp(ID, tableCell.attr("id"));
		cell.addProp(A_HREF, tableCell.getElementsByAttribute("href").attr("href"));
		cell.addProp(ABBR_TITLE, tableCell.getElementsByTag("abbr").attr("title"));
		cell.addProp(ABBR_TEXT, tableCell.getElementsByTag("abbr").text().trim());
		cell.addProp(RESTRRICTED, Boolean.valueOf(tableCell
				.getElementsByClass("restricted") != null && !tableCell
						.getElementsByClass("restricted").isEmpty()).toString());
		cell.addProp(INPUT_NAME, tableCell.getElementsByTag("input").attr("name"));
		cell.addProp(INPUT_VAL, tableCell.getElementsByTag("input").attr("value"));
		cell.addProp(INPUT_TYPE, tableCell.getElementsByTag("input").attr("type"));
		cell.addProp(A_HREF, tableCell.getElementsByAttribute("href").attr("href"));
		cell.addProp(TEXT, getTextFromTd(tableCell));
		row.addCell(cell);
	}
	
	private static String getTextFromTd(Element td) {
		if (td.getElementsByAttribute("href") != null 
				&& !td.getElementsByAttribute("href").isEmpty()) {
			return  td.getElementsByAttribute("href").text().trim();
		} else {
			return td.text().replace("\u00a0", "").trim();
		}
	}
}
