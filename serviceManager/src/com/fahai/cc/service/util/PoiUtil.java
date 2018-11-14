package com.fahai.cc.service.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fahai.cc.service.customer.entity.CustomerQueryCount;

public class PoiUtil {
	
	public Workbook monthReportExport(String titleStr,List<CustomerQueryCount> dataList) throws IOException{
		titleStr = "                         "+titleStr+"接口客户月度报表";
		Workbook workbook = reportExport(titleStr, "月别报表", "yyyy-MM", dataList);
		return workbook;
	}
	public Workbook dailyReportExport(String titleStr,List<CustomerQueryCount> dataList) throws IOException{
		titleStr = "                     "+titleStr+"接口客户日别报表";
		Workbook workbook = reportExport(titleStr, "日别报表", "yyyy-MM-dd", dataList);
		return workbook;
	}
	public Workbook dailyCustomerReportExport(String title, List<CustomerQueryCount> dataList) throws IOException {
		title = "                   "+title;
		Workbook workbook = reportExport(title, "日别报表", "yyyy-MM-dd", dataList);
		return workbook;
	}
	public Workbook monthCustomerReportExport(String title, List<CustomerQueryCount> dataList) throws IOException {
		title = "                   "+title;
		Workbook workbook = reportExport(title, "月别报表", "yyyy-MM", dataList);
		return workbook;
	}
	public Workbook yearCustomerReportExport(String title, List<CustomerQueryCount> dataList) throws IOException {
		title = "                   "+title;
		Workbook workbook = reportExport(title, "年度报表", "yyyy", dataList);
		return workbook;
	}
	private Workbook reportExport(String titleStr,String sheetName,String dateFormat,List<CustomerQueryCount> dataList) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			workbook.setSheetName(0, sheetName);
			//标题字体
			XSSFFont titleFont = workbook.createFont();
			titleFont.setFontName("宋体");
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 16);
			
			XSSFCellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
			titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
			
			titleCellStyle.setFont(titleFont);
			
			//合并单元格
			CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, 0, 8);
			
			sheet.addMergedRegion(cellRangeAddress);
			
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 700);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue(titleStr);
			titleCell.setCellStyle(titleCellStyle);
			
			
			XSSFDrawing drawingPatriarch = (XSSFDrawing) sheet.createDrawingPatriarch();//添加图片
			String path = this.getClass().getClassLoader().getResource("").getPath()+"sylogo.png";
			setPicture(workbook, drawingPatriarch, path, 0, 0, 2, 2);
			
			//表头
			XSSFFont tableTopFont = workbook.createFont();
			tableTopFont.setFontName("宋体");
			tableTopFont.setBold(true);
			tableTopFont.setFontHeightInPoints((short) 10);
			XSSFCellStyle tableTopCellStyle = workbook.createCellStyle();
			tableTopCellStyle.setFont(tableTopFont);
			tableTopCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			tableTopCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			tableTopCellStyle.setBorderBottom(tableTopCellStyle.BORDER_THIN);//下边框
			tableTopCellStyle.setBorderLeft(tableTopCellStyle.BORDER_THIN);//左边框
			tableTopCellStyle.setBorderTop(tableTopCellStyle.BORDER_THIN);//上边框
			tableTopCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			tableTopCellStyle.setAlignment(HorizontalAlignment.CENTER);
			sheet.setColumnWidth(1, 18*256);
			sheet.setColumnWidth(2, 15*256);
			sheet.setColumnWidth(3, 15*256);
			sheet.setColumnWidth(4, 15*256);
			sheet.setColumnWidth(5, 15*256);
			sheet.setColumnWidth(6, 15*256);
			sheet.setColumnWidth(7, 15*256);
			sheet.setColumnWidth(8, 15*256);
			
			
			//表体
			XSSFFont tableBodyFont = workbook.createFont();
			tableBodyFont.setFontName("宋体");
			tableBodyFont.setFontHeightInPoints((short) 10);
			
			XSSFCellStyle tableBodyCellStyle = workbook.createCellStyle();
			tableBodyCellStyle.setFont(tableBodyFont);
			tableBodyCellStyle.setBorderBottom(tableBodyCellStyle.BORDER_THIN);//下边框
			tableBodyCellStyle.setBorderLeft(tableBodyCellStyle.BORDER_THIN);//左边框
			tableBodyCellStyle.setBorderTop(tableBodyCellStyle.BORDER_THIN);//上边框
			tableBodyCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			tableBodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
			//表中数字添加千位符
			XSSFCellStyle tableBodyNumCellStyle = workbook.createCellStyle();
			tableBodyNumCellStyle.setFont(tableBodyFont);
			tableBodyNumCellStyle.setBorderBottom(tableBodyCellStyle.BORDER_THIN);//下边框
			tableBodyNumCellStyle.setBorderLeft(tableBodyCellStyle.BORDER_THIN);//左边框
			tableBodyNumCellStyle.setBorderTop(tableBodyCellStyle.BORDER_THIN);//上边框
			tableBodyNumCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			tableBodyNumCellStyle.setAlignment(HorizontalAlignment.CENTER);
			XSSFDataFormat dataFormat = workbook.createDataFormat();
			tableBodyNumCellStyle.setDataFormat(dataFormat.getFormat("#,##0"));
			
			XSSFCellStyle mergeCellStyle = workbook.createCellStyle();
			mergeCellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
			mergeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
			mergeCellStyle.setFont(tableBodyFont);
			mergeCellStyle.setBorderBottom(tableBodyCellStyle.BORDER_THIN);//下边框
			mergeCellStyle.setBorderLeft(tableBodyCellStyle.BORDER_THIN);//左边框
			mergeCellStyle.setBorderTop(tableBodyCellStyle.BORDER_THIN);//上边框
			mergeCellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
			
			Row tableTopRow = sheet.createRow(2);
			tableTopRow.setHeight((short) 500);
			
			Cell tableTopCell = null;
			for (int i = 0; i < 9; i++) {
				tableTopCell = tableTopRow.createCell(i);
				tableTopCell.setCellStyle(tableTopCellStyle);
				if(i==0){
					tableTopCell.setCellValue("序号");
				}
				if(i==1){
					tableTopCell.setCellValue("客户名称");
				}
				if(i==2){
					tableTopCell.setCellValue("查询时间");
				}
				if(i==3){
					tableTopCell.setCellValue("接口");
				}
				if(i==4){
					tableTopCell.setCellValue("查询类型");
				}
				if(i==5){
					tableTopCell.setCellValue("总量");
				}
				if(i==6){
					tableTopCell.setCellValue("查得");
				}
				if(i==7){
					tableTopCell.setCellValue("去重");
				}
				if(i==8){
					tableTopCell.setCellValue("失败");
				}
				
			}
			
			
			Row tableBodyRow = null;
			if (dataList != null && dataList.size()>0) {
				int amountSU = 0;
				int amountQG = 0;
				int amountDI = 0;
				int amountFA = 0;
				
				int markBit = 0;
				
				List<Map<String, Object>> complexSetList = new ArrayList<>();
				
				SimpleDateFormat format = new SimpleDateFormat(dateFormat);
				for (int i = 0; i < dataList.size()+1; i++) {
					tableBodyRow = sheet.createRow(3+i);
					tableBodyRow.setHeight((short) 400);
					if(i==dataList.size()){
						Cell tableBodyCell0 = tableBodyRow.createCell(0);
						tableBodyCell0.setCellStyle(tableBodyCellStyle);
						tableBodyCell0.setCellValue("合计");
						
						Cell tableBodyCell1 = tableBodyRow.createCell(1);
						tableBodyCell1.setCellStyle(tableBodyCellStyle);
						
						Cell tableBodyCell2 = tableBodyRow.createCell(2);
						tableBodyCell2.setCellStyle(tableBodyCellStyle);
						
						Cell tableBodyCell3 = tableBodyRow.createCell(3);
						tableBodyCell3.setCellStyle(tableBodyCellStyle);
						
						
						Cell tableBodyCell4 = tableBodyRow.createCell(4);
						tableBodyCell4.setCellStyle(tableBodyCellStyle);
						
						Cell tableBodyCell5 = tableBodyRow.createCell(5);
						tableBodyCell5.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell5.setCellValue(amountSU);
						
						Cell tableBodyCell6 = tableBodyRow.createCell(6);
						tableBodyCell6.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell6.setCellValue(amountQG);
						
						Cell tableBodyCell7 = tableBodyRow.createCell(7);
						tableBodyCell7.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell7.setCellValue(amountDI);
						
						Cell tableBodyCell8 = tableBodyRow.createCell(8);
						tableBodyCell8.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell8.setCellValue(amountFA);
					}else{
						if (i<dataList.size()-1&&dataList.get(i).getCompanyName().equals(dataList.get(i+1).getCompanyName())&&
								format.format(dataList.get(i).getQueryTime()).equals(format.format(dataList.get(i+1).getQueryTime()))) {
							markBit += 1;
							
						}else{
							Map<String, Object> paramMap = new HashMap<>();
							paramMap.put("companyName", dataList.get(i).getCompanyName());
							paramMap.put("queryTime", dataList.get(i).getQueryTime());
							paramMap.put("startRow", i+3-markBit);
							if (markBit>0) {
								markBit = 0;
								paramMap.put("endRow", i+3);
							}
							complexSetList.add(paramMap);
						}
						Cell tableBodyCell0 = tableBodyRow.createCell(0);
						tableBodyCell0.setCellStyle(tableBodyCellStyle);
//						tableBodyCell0.setCellValue(String.valueOf(i+1));
						
						Cell tableBodyCell1 = tableBodyRow.createCell(1);
						tableBodyCell1.setCellStyle(tableBodyCellStyle);
//						tableBodyCell1.setCellValue(dataList.get(i).getCompanyName());
						
						
						Cell tableBodyCell2 = tableBodyRow.createCell(2);
						tableBodyCell2.setCellStyle(tableBodyCellStyle);
						
						Cell tableBodyCell3 = tableBodyRow.createCell(3);
						tableBodyCell3.setCellStyle(tableBodyCellStyle);
						tableBodyCell3.setCellValue(dataList.get(i).getInterfaceCode());
						
						Cell tableBodyCell4 = tableBodyRow.createCell(4);
						tableBodyCell4.setCellStyle(tableBodyCellStyle);
						tableBodyCell4.setCellValue(dataList.get(i).getSearchType());
						
						Cell tableBodyCell5 = tableBodyRow.createCell(5);
						tableBodyCell5.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell5.setCellValue(dataList.get(i).getTotalSU());
						amountSU += dataList.get(i).getTotalSU();
						
						Cell tableBodyCell6 = tableBodyRow.createCell(6);
						tableBodyCell6.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell6.setCellValue(dataList.get(i).getTotalQG());
						amountQG += dataList.get(i).getTotalQG();
						
						Cell tableBodyCell7 = tableBodyRow.createCell(7);
						tableBodyCell7.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell7.setCellValue(dataList.get(i).getTotalDI());
						amountDI += dataList.get(i).getTotalDI();
						
						Cell tableBodyCell8 = tableBodyRow.createCell(8);
						tableBodyCell8.setCellStyle(tableBodyNumCellStyle);
						tableBodyCell8.setCellValue(dataList.get(i).getTotalFA());
						amountFA += dataList.get(i).getTotalFA();
						
//						Cell tableBodyCell8 = tableBodyRow.createCell(8);
//						tableBodyCell8.setCellStyle(tableBodyCellStyle);
//						tableBodyCell8.setCellValue(format.format(dataList.get(i).getQueryTime()));
					}
					
					
				}
				if (complexSetList.size()>0) {

					for (int i = 0; i < complexSetList.size(); i++) {
						Row row = sheet.getRow(Integer.valueOf(complexSetList.get(i).get("startRow").toString()));
						Cell cellIndex = row.getCell(0);
						Cell cellCompanyName = row.getCell(1);
						Cell cellQueryTime = row.getCell(2);
						
						cellIndex.setCellValue(i+1);
						cellCompanyName.setCellValue(complexSetList.get(i).get("companyName").toString());
						cellQueryTime.setCellValue(format.format(complexSetList.get(i).get("queryTime")));
						
						if (complexSetList.get(i).containsKey("endRow")) {
							cellIndex.setCellStyle(mergeCellStyle);
							cellCompanyName.setCellStyle(mergeCellStyle);
							cellQueryTime.setCellStyle(mergeCellStyle);
							CellRangeAddress cellRangeAddressIndex = new CellRangeAddress(
									Integer.valueOf(complexSetList.get(i).get("startRow").toString()), 
									Integer.valueOf(complexSetList.get(i).get("endRow").toString()), 
									0, 0);
							
							sheet.addMergedRegion(cellRangeAddressIndex);
							CellRangeAddress cellRangeAddressCompanyName = new CellRangeAddress(
									Integer.valueOf(complexSetList.get(i).get("startRow").toString()), 
									Integer.valueOf(complexSetList.get(i).get("endRow").toString()), 
									1, 1);
							
							sheet.addMergedRegion(cellRangeAddressCompanyName);
							CellRangeAddress cellRangeAddressQueryTime = new CellRangeAddress(
									Integer.valueOf(complexSetList.get(i).get("startRow").toString()), 
									Integer.valueOf(complexSetList.get(i).get("endRow").toString()), 
									2, 2);
							
							sheet.addMergedRegion(cellRangeAddressQueryTime);
							
						}
					}
				}
				
			}else{
				tableBodyRow = sheet.createRow(3);
				tableBodyRow.setHeight((short) 400);
				tableBodyRow.createCell(0).setCellValue("暂无数据");
			}
			
			
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return workbook;
	}
	
	private static void setPicture(XSSFWorkbook wb, XSSFDrawing patriarch, String pic, int iRowStart, int iColStart, int iRowStop, int iColStop) throws IOException {
		// 判断文件是否存在
		File imgFile = new File(pic);
		if (imgFile.exists()) {
			// 图片处理
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			BufferedImage bufferImg = ImageIO.read(imgFile);
			ImageIO.write(bufferImg, "png", byteArrayOut);
			
			// 左,上(0-255),右(0-1023),下
			XSSFClientAnchor anchor = new XSSFClientAnchor(20, 1, 1018, 0, (short) (iColStart), iRowStart, (short) (iColStop), iRowStop);
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		PoiUtil poiUtil = new PoiUtil();
		
		List<CustomerQueryCount> dataList = new ArrayList<>();
		CustomerQueryCount customerQueryCount = new CustomerQueryCount();
		customerQueryCount.setCompanyName("法海风控");
		customerQueryCount.setInterfaceCode("query");
		customerQueryCount.setQueryTime(new Date());
		customerQueryCount.setSearchType("common");
		customerQueryCount.setTotalDI(10);
		customerQueryCount.setTotalFA(20);
		customerQueryCount.setTotalQG(30);
		customerQueryCount.setTotalSU(40);
		dataList.add(customerQueryCount);
		Workbook workbook = poiUtil.monthReportExport("2017-09至2017-10", dataList);
		File file = new File("E:\\newExport.xlsx");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		
		fos.close();
	}
	
}
