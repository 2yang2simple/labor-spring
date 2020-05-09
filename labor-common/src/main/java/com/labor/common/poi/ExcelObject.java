package com.labor.common.poi;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.extractor.POIOLE2TextExtractor;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;

import com.labor.common.util.FileUtil;
import com.labor.common.util.TokenUtil;

public class ExcelObject {
	
	public static void main(String[] args) {
		
		String doc = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\ProductQuotation.xls";
		String html = "ProductQuotation.html";
		String path = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\";
		
		try {
			
			ExcelObject word = new ExcelObject(doc);
			System.err.println(word.convertXls2HtmlString(null));
			word.convertXls2HtmlFile(null);		
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String fileFullName = "";


	public ExcelObject(String fileFullName) {
		this.fileFullName = fileFullName;

	}
	
	public String xls(InputStream is) throws IOException{
		StringBuilder content = new StringBuilder();
		HSSFWorkbook workbook = new HSSFWorkbook(is);// 创建对Excel工作簿文件的引用
		for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
			if (null != workbook.getSheetAt(numSheets)) {
				HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet.getLastRowNum(); rowNumOfSheet++) {
					if (null != aSheet.getRow(rowNumOfSheet)) {
						HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
						for (short cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
							if (null != aRow.getCell(cellNumOfRow)) {
								HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
								if (convertCell(aCell).length() > 0) {
									content.append(convertCell(aCell));
								}
							}
							content.append("\n");
						}
					}
				}
			}
		}
		return content.toString();

	}
	private String convertCell(Cell cell) {
		NumberFormat formater = NumberFormat.getInstance();
		formater.setGroupingUsed(false);
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}

		switch (cell.getCellType()) {
		case NUMERIC:
			cellValue = formater.format(cell.getNumericCellValue());
			break;
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case BLANK:
			cellValue = cell.getStringCellValue();
			break;
		case BOOLEAN:
			cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
			break;
		case ERROR:
			cellValue = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			cellValue = "";
		}
		return cellValue.trim();
	}
	

	public static POIXMLTextExtractor xlsx(OPCPackage opcPackage) throws XmlException, OpenXML4JException, IOException {
		return new XSSFExcelExtractor(opcPackage);
	}
	
	
	public String convertXls2HtmlString() {
		return convertXls2HtmlString(null);

	}

	public void convertXls2HtmlFile(PicturesManager picturesManager){
		String content = convertXls2HtmlString(picturesManager);
		FileUtil.writeFile(content, fileFullName + ".html");


	}

	public String convertXls2HtmlString(PicturesManager picturesManager){
		String ret = null;
		FileInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			is = new FileInputStream(fileFullName);
			os = new ByteArrayOutputStream();

			
			HSSFWorkbook excelBook = new HSSFWorkbook(is);
			ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

			excelToHtmlConverter.processWorkbook(excelBook);
			Document htmlDocument = excelToHtmlConverter.getDocument();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(os);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);

			ret = new String(os.toByteArray());
			
		} catch (FileNotFoundException e) {
			LogManager.getLogger().error("", e);
		} catch (IOException e) {
			LogManager.getLogger().error("", e);
		} catch (TransformerException e) {
			LogManager.getLogger().error("", e);
		} catch (ParserConfigurationException e) {
			LogManager.getLogger().error("", e);
		} finally {
			try {
				if (is!=null) {
					is.close();
				}
				if (os!=null) {
					os.close();
				}
			} catch (IOException ioe) {
				LogManager.getLogger().error("", ioe);
			}
		}
		return ret;

	}
	
	public PicturesManager getPicturesManager() {
		return new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				
				//do some upload data;
				// <img src="/attachments/filename-774fd40d84414b03b7aa6be1482b529a">
				suggestedName = fileFullName + "-" + TokenUtil.generateUUID() + "." + pictureType;
				FileUtil.writeFile(content, suggestedName);
				return suggestedName;
			}
		};
	}
}
