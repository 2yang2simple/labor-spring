package com.labor.common.poi;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;

import com.labor.base.acl.UserVO;
import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.BeanUtil;
import com.labor.common.util.StringUtil;

import java.io.*;
import java.util.HashMap;
import java.util.List;


public class ExcelUtil {

	public static void main(String[] args) throws Exception {

		SubjectServiceIntf subjectService = (SubjectServiceIntf) new ServiceTransactionProxy()
				.bind(new SubjectServiceImpl());
		
		UserVO uvo = new UserVO();
		uvo.setRolename("eererer");
		System.out.println(BeanUtil.getValueByKey(uvo, "rolename"));
		
		HashMap hm = new HashMap();
		
		List dataList = subjectService.listSubject(hm);
		System.out.println(dataList.get(0).getClass().getName());
	
        String[] headRowNames = new String[]{
					        "sub_id",
							"sub_name1",
							"sub_name2",
							"sub_name3",
							"sub_name4",
							"sub_name5",
							"sub_name6",
							"sub_name7",
							"sub_name8",
							"sub_name9",
							"active_status",
							"creation_date",
							"last_update_date"};

        String xlsname = String.valueOf(System.currentTimeMillis());
		String fileName = "D:\\" + xlsname + ".xls";

		FileOutputStream out = new FileOutputStream(fileName);;
        ExcelUtil.export(headRowNames, dataList, out,5000);
	}

	
	public static void export(String[] headRowNames, List dataList, OutputStream out, int max) {
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();

			// create the head
			HSSFRow headRow = sheet.createRow(0);

			// set the head row names to the head row;
			int colCount = headRowNames.length;
			for (int n = 0; n < colCount; n++) {
				HSSFCell headRowCell = headRow.createCell(n);
				headRowCell.setCellType(CellType.STRING);
				HSSFRichTextString headRowCellValue = new HSSFRichTextString(headRowNames[n]);
				headRowCell.setCellValue(headRowCellValue);
			}

			// export max 5000 rows, to avoid outofmemory
			int rowCount = dataList.size();
			if (rowCount > max) {
				rowCount = max;
			}
			for (int i = 0; i < rowCount; i++) {
				HSSFRow row = sheet.createRow(i + 1);
				for (int j = 0; j < colCount; j++) {				
					HSSFCell cell = null;
					cell = row.createCell(j, CellType.STRING);
					cell.setCellValue(StringUtil.safeToString(BeanUtil.getValueByKey(dataList.get(i),headRowNames[j]))); //bean in list
//					HashMap hm = (HashMap)dataList.get(i);
//					cell.setCellValue(StringUtil.safeToString(hm.get(headRowNames[j]))); //hashmap in list;
				}
			}
			// adjust the column width
			for (int colNum = 0; colNum < colCount; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow;
					// if current row never be used;
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == CellType.STRING) {
							int length = currentCell.getStringCellValue().getBytes().length;
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
				}
			}

			if (workbook != null) {
				workbook.write(out);
			}

//		} catch (FileNotFoundException fnfe) {
//			LogManager.getLogger().error("", fnfe);
		} catch (IOException ioe) {
			LogManager.getLogger().error("", ioe);
//		} catch (Exception e) {
//			LogManager.getLogger().error("",e);
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException ioe) {
				LogManager.getLogger().error("", ioe);
			}
//			try {
//				if (out != null) {
//					out.flush();
//					out.close();
//				}	
//			} catch (IOException ioe) {
//				LogManager.getLogger().error("", ioe);
//			}
		}

	}

}