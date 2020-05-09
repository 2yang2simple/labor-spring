package com.labor.common.poi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;

import com.labor.common.util.FileUtil;
import com.labor.common.util.TokenUtil;

/****
 * 
Component	Application type	Maven artifactId	Notes
POIFS	OLE2 Filesystem	poi	Required to work with OLE2 / POIFS based files
HPSF	OLE2 Property Sets	poi	 
HSSF	Excel XLS	poi	For HSSF only, if common SS is needed see below
HSLF	PowerPoint PPT	poi-scratchpad	 
HWPF	Word DOC	poi-scratchpad	 
HDGF	Visio VSD	poi-scratchpad	 
HPBF	Publisher PUB	poi-scratchpad	 
HSMF	Outlook MSG	poi-scratchpad	 
DDF	Escher common drawings	poi	 
HWMF	WMF drawings	poi-scratchpad	 
OpenXML4J	OOXML	poi-ooxml plus either poi-ooxml-schemas or
ooxml-schemas and ooxml-security	See notes below for differences between these options
XSSF	Excel XLSX	poi-ooxml	 
XSLF	PowerPoint PPTX	poi-ooxml	 
XWPF	Word DOCX	poi-ooxml	 
XDGF	Visio VSDX	poi-ooxml	 
Common SL	PowerPoint PPT and PPTX	poi-scratchpad and poi-ooxml	SL code is in the core POI jar, but implementations are in poi-scratchpad and poi-ooxml.
Common SS	Excel XLS and XLSX	poi-ooxml	WorkbookFactory and friends all require poi-ooxml, not just core poi
 * @author Administrator
 *
 */
public class OfficeReader {
	public static String doc = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc";

	public static String html = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.html";

	public static String path = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\";
	
	public static void main(String[] args) {
		try {
			
			System.err.println(doc.replaceFirst("", ""));
			
//			String dd = "d:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc";
//			System.err.println(""+dd.indexOf("d:\\opt\\dm\\labor\\documents"));
			
//			WordObject word = new WordObject(doc);
//			word.convertDoc2HtmlFile(word.getPicturesManager());
			
//			System.err.println(extractOfficeHtml("D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc"));
		
			//extractOfficeHtml(getPicturesManager(), path,doc,html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void test() {
		System.err.println("xxxx");
	}
	
	
	public static String extractHtml(String fileFullName, PicturesManager picturesManager) {
		String ret = null;
		String filetype = FileUtil.getFileType(fileFullName);
		filetype = (filetype == null) ? "" : filetype.toLowerCase();
		switch (filetype) {
			case "doc":
				WordObject word = new WordObject(fileFullName);
				ret = word.convertDoc2HtmlString(picturesManager);
				break;
			case "xls":
				ExcelObject excel = new ExcelObject(fileFullName);
				ret = excel.convertXls2HtmlString(null);
				break;
			default:
				ret = extractText(fileFullName);
				break;
		}

		return ret;
	}

	public static String extractText(String fileFullName) {
		String ret = null;
		try {
			ret = extractOfficeText(fileFullName);
			ret = ret==null?extractOffice2007Text(fileFullName):ret;
		} catch (Exception e) {
			LogManager.getLogger().error("File:"+fileFullName,e);
		}
		return ret;
	}
	
	public static String extractOfficeText(String fileFullName) {
		String ret = null;
		String filetype = FileUtil.getFileType(fileFullName);
		FileInputStream fis = null; 
		POIOLE2TextExtractor extractor = null;
		try {
			if (filetype!=null) {
				fis = new FileInputStream(new File(fileFullName));
				if ("doc".equals(filetype.toLowerCase())) {
					extractor = doc(fis);
				} else if ("xls".equals(filetype.toLowerCase())) {
					return xls(fis);
				} else if ("ppt".equals(filetype.toLowerCase())) {
					extractor = ppt(fis);
				}
			}
			if (extractor!=null) {
				ret = extractor.getText();
			}
		} catch (FileNotFoundException e) {
			LogManager.getLogger().error("", e);
		} catch (IOException e) {
			LogManager.getLogger().error("", e);

		} finally {
			try {
				if (fis!=null) {
					fis.close();
				}
				if (extractor!=null) {
					extractor.close();
				}
			} catch (IOException ioe) {
				LogManager.getLogger().error("", ioe);
			}
		}
		return ret;
		
	}
	
	public static String extractOffice2007Text(String fileFullName) {
		String ret = null;
		String filetype = FileUtil.getFileType(fileFullName);
		OPCPackage opcPackage =null;
		POIXMLTextExtractor extractor = null;
		try {
			if (filetype!=null) {
				opcPackage = POIXMLDocument.openPackage(fileFullName);
				if ("docx".equals(filetype.toLowerCase())) {
					extractor = docx(opcPackage);
				} else if ("xlsx".equals(filetype.toLowerCase())) {
					extractor = xlsx(opcPackage);
				} else if ("pptx".equals(filetype.toLowerCase())) {
					extractor = pptx(opcPackage);
				}
			}
			if (extractor!=null) {
				ret = extractor.getText();
			}
	
		} catch (XmlException xe) {
			LogManager.getLogger().error("", xe);
		} catch (IOException ioe) {
			LogManager.getLogger().error("", ioe);
		} catch (OpenXML4JException oxje) {
			LogManager.getLogger().error("", oxje);
		} finally {
			try {
				if (opcPackage!=null) {
					opcPackage.close();
				}
				if (extractor!=null) {
					extractor.close();
				}
			} catch (IOException ioe) {
				LogManager.getLogger().error("", ioe);
			}
		}
		return ret;
	}
	

	
	public static POIOLE2TextExtractor doc(InputStream is) throws IOException{
		return new WordExtractor(is);
	}
	public static String xls(InputStream is) throws IOException{
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
	public static POIOLE2TextExtractor ppt(InputStream is) throws IOException{
		return new PowerPointExtractor(is);
	}
	
	public static POIXMLTextExtractor docx(OPCPackage opcPackage) throws XmlException, OpenXML4JException, IOException {
		return new XWPFWordExtractor(opcPackage);
	}
	
	public static POIXMLTextExtractor xlsx(OPCPackage opcPackage) throws XmlException, OpenXML4JException, IOException {
		return new XSSFExcelExtractor(opcPackage);
	}
	
	public static POIXMLTextExtractor pptx(OPCPackage opcPackage) throws XmlException, OpenXML4JException, IOException {
		return new XSLFPowerPointExtractor(opcPackage);
	}

	private static String convertCell(Cell cell) {
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
	
}
