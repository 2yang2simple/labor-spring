package com.labor.common.poi;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;

import com.labor.common.util.FileUtil;
import com.labor.common.util.TokenUtil;

public class WordObject {

	
	public static void main(String[] args) {
		
		String doc = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc";
		String html = "Software Acquisition Plan.html";
		String path = "D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\";
		
		try {
			
			WordObject word = new WordObject(doc);
			word.convertDoc2HtmlFile(word.getPicturesManager());
			
//			System.err.println(extractOfficeHtml("D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc"));
		
			//extractOfficeHtml(getPicturesManager(), path,doc,html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String fileFullName = "";


	public WordObject(String fileFullName) {
		this.fileFullName = fileFullName;

	}
	
	public POIOLE2TextExtractor doc(InputStream is) throws IOException{
		return new WordExtractor(is);
	}
	
	public POIXMLTextExtractor docx(OPCPackage opcPackage) throws XmlException, OpenXML4JException, IOException {
		return new XWPFWordExtractor(opcPackage);
	}
	
	public String convertDoc2HtmlString() {
		return convertDoc2HtmlString(null);

	}

	public void convertDoc2HtmlFile(PicturesManager picturesManager){
		String content = convertDoc2HtmlString(picturesManager);
		FileUtil.writeFile(content, fileFullName + ".html");


	}

	public String convertDoc2HtmlString(PicturesManager picturesManager){
		String ret = null;
		FileInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			is = new FileInputStream(fileFullName);
			os = new ByteArrayOutputStream();
			
			HWPFDocument wordDocument = new HWPFDocument(is);
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			if (picturesManager!=null) {
				wordToHtmlConverter.setPicturesManager(picturesManager);
			}
			wordToHtmlConverter.processDocument(wordDocument);
	
			Document htmlDocument = wordToHtmlConverter.getDocument();
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
