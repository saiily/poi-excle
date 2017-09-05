package com.saiily.excle.utils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.saiily.excle.annotation.ExcelColumn;
import com.saiily.excle.annotation.ExcelRow;
import com.saiily.excle.enums.DealType;
import com.saiily.excle.exception.ReportException;
import com.saiily.excle.processor.BaseProcessor;
import com.saiily.excle.processor.WriteProcessor;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ExcelWriteUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExcelWriteUtils.class);

	public static void writeReport(String filePath, WriteProcessor<?>... sheetProcessors) {
		if (ValidateUtils.isExcel(filePath)) {
			writeReport(getOutputStream(filePath), sheetProcessors);
		}else{
			throw new ReportException("生成的文件不是EXCLE格式，请确认生成文件的格式是否正确");
		}
	}

	public static void writeReport(OutputStream outputStream,
			WriteProcessor<?>... sheetProcessors) {

		SXSSFWorkbook workbook = createWorkBook();
		try {
			for (WriteProcessor<?> sheetProcessor : sheetProcessors) {

				String sheetName = sheetProcessor.getSheetName();
				Integer sheetIndex = sheetProcessor.getSheetIndex();
				// 生成表格sheet
				Sheet sheet = createSheet(workbook, sheetName, sheetIndex);
				sheetProcessor.setSheet(sheet);
				Class<?> clazz = (Class<?>) ((ParameterizedTypeImpl) sheetProcessor.getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
				// 创建表头
				createTableHead(sheet, clazz);
				// 获取起始行读数据
				int writeRowIndex = sheetProcessor.getRowStartIndex();
				// 获取要写入的数据
				List<?> dataList = sheetProcessor.getList();
				// 设置单元格样式
				Map<String, CellStyle> cellStyles = getCellStyle(workbook, clazz);

				if (dataList != null && dataList.size() > 0) {
					// 默认分页数
					int pageSize = BaseProcessor.PAGE_SIZE;
					int size = dataList.size();
					// 分批数
					int part = (size % pageSize == 0) ? (size / pageSize) : (size / pageSize + 1);
					logger.info("共有 ：{}条数据，分为 ：{}批", size, part);
					for (int i = 0; i < part; i++) {
						List<?> listPage = new ArrayList<>();
						if (i == part - 1) {
							writeRowIndex = write(sheetProcessor, dataList, clazz, cellStyles, writeRowIndex);
							dataList.clear();
						} else {
							//取pageSize条
							listPage = dataList.subList(0, pageSize);
							writeRowIndex = write(sheetProcessor, listPage, clazz, cellStyles, writeRowIndex);
							//剔除pageSize条
						    dataList.subList(0, pageSize).clear();
						}
					}
				}
			}
			workbook.write(outputStream);
			outputStream.flush();
			logger.info("生成Excle文件成功~");
		} catch (Exception e) {
			logger.error("生成Excle文件时异常~", e);
			throw new ReportException("生成Excle文件时异常~", e);
		} finally {
			close(workbook,outputStream);
		}
	}

	/**
	 * 创建工作簿页签
	 * 
	 * @param workbook
	 * @param sheetName
	 * @param sheetIndex
	 * @return
	 */
	private static Sheet createSheet(Workbook workbook, String sheetName, Integer sheetIndex) {
		Sheet sheet;
		if (sheetName != null) {
			sheet = workbook.createSheet(sheetName);
			if (sheetIndex != null) {
				workbook.setSheetOrder(sheetName, sheetIndex);
			}
		} else if (sheetIndex != null) {
			sheet = workbook.createSheet();
			workbook.setSheetOrder(sheet.getSheetName(), sheetIndex);
		} else {
			sheet = workbook.createSheet();
			workbook.setSheetOrder(sheet.getSheetName(), 0);
		}
		return sheet;
	}

	private static int write(WriteProcessor<?> sheetProcessor, List<?> listPage, Class<?> clazz,
			Map<String, CellStyle> cellStyles, int writeRowIndex) throws Exception {
		// 获取sheet
		Sheet sheet = sheetProcessor.getSheet();
		ExcelRow rowData = clazz.getAnnotation(ExcelRow.class);
		Field[] declaredFields = clazz.getDeclaredFields();

		for (Object obj : listPage) {
			// 获取row列表
			List<Row> rowList = getRowList(writeRowIndex, sheet, rowData);

			for (Field field : declaredFields) {
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
				Object val = pd.getReadMethod().invoke(obj);
				ExcelColumn column = field.getAnnotation(ExcelColumn.class);
				if (null == column) {
					continue;
				}
				Cell cell = rowList.get(column.rowIndex()).createCell(column.colIndex());
				try {
					// 判断是否跨行
					if (rowData != null && column.isAcross()) {
						CellRangeAddress cra = new CellRangeAddress(writeRowIndex,
								writeRowIndex + rowData.rowAcross() - 1, column.colIndex(), column.colIndex());
						sheet.addMergedRegion(cra);
					}
					setCell(cell, field.getType(), val);
					CellStyle cellStyle = cellStyles.get(field.getName());
					if (cellStyle != null) {
						sheet.setColumnWidth(column.colIndex(), (short) 3600);
						cell.setCellStyle(cellStyle);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (column.dealType().equals(DealType.CONTINUE_CELL)) {
						continue;
					}
					if (column.dealType().equals(DealType.CONTINUE_ROW)) {
						break;
					}
					throw new ReportException("deal cell exception", e);
				}
			}
			if (rowData != null) {
				writeRowIndex = writeRowIndex + rowData.rowAcross();
			} else {
				writeRowIndex++;
			}
		}
		return writeRowIndex;
	}

	/**
	 * 创建行列表
	 * 
	 * @param writeRowIndex
	 * @param sheet
	 * @param rowData
	 * @return
	 */
	private static List<Row> getRowList(int writeRowIndex, Sheet sheet, ExcelRow rowData) {
		List<Row> rowList = new ArrayList<>();
		for (int i = 0; rowData != null && i < rowData.rowAcross() || i < 1; i++) {
			Row row = sheet.getRow(writeRowIndex + i);
			if (row == null) {
				row = sheet.createRow(writeRowIndex + i);
			}
			rowList.add(row);
		}
		return rowList;
	}

	/**
	 * 设置单元格数据
	 * 
	 * @param cell
	 * @param clazz
	 * @param val
	 */
	private static void setCell(Cell cell, Class<?> clazz, Object val) {
		try {
			if (clazz.equals(short.class) || clazz.equals(Short.class)) {
				cell.setCellValue((Short) val);
			} else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
				cell.setCellValue((Integer) val);
			} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
				cell.setCellValue((Long) val);
			} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
				cell.setCellValue((Float) val);
			} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
				cell.setCellValue((Double) val);
			} else if (clazz.equals(Date.class)) {
				cell.setCellValue((Date) val);
			} else if (clazz.equals(BigDecimal.class)) {
				cell.setCellValue(((BigDecimal) val).doubleValue());
			} else {
				cell.setCellValue(StringUtils.isEmpty(val) ? "" : val.toString());
			}
		} catch (Exception e) {
			cell.setCellValue(StringUtils.isEmpty(val) ? "" : val.toString());
		}

	}

	/**
	 * 设置单元格格式
	 * 
	 * @param workbook
	 * @param clazz
	 * @return
	 */
	private static Map<String, CellStyle> getCellStyle(Workbook workbook, Class<?> clazz) {
		Map<String, CellStyle> cellStyles = new HashMap<>();
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			ExcelColumn column = field.getAnnotation(ExcelColumn.class);
			if (null == column) {
				continue;
			}
			if (!StringUtils.isEmpty(column.format())) {
				CellStyle cellStyle = workbook.createCellStyle();
				// 设置左对齐
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
				DataFormat dataFormat = workbook.createDataFormat();
				cellStyle.setDataFormat(dataFormat.getFormat(column.format()));
				cellStyles.put(field.getName(), cellStyle);
			}
		}
		return cellStyles;
	}

	/**
	 * 根据excle类型生成工作簿对象
	 * 
	 * @param excelType
	 *            excle类型
	 * @return
	 */
	private static SXSSFWorkbook createWorkBook() {
		return new SXSSFWorkbook(500);
	}

	/**
	 * 创建表格的表头信息
	 * 
	 * @param sheet
	 * @param clazz
	 */
	private static void createTableHead(Sheet sheet, Class<?> clazz) {

		Row headRow = sheet.createRow(0);
		sheet.setColumnWidth(2, 20 * 256);
		headRow.setHeightInPoints(20);
		// 获取所有字段
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			ExcelColumn columnData = field.getAnnotation(ExcelColumn.class);
			if (null == columnData) {
				continue;
			}
			Cell cell = headRow.createCell(columnData.colIndex());
			// 设置表头
			cell.setCellValue("".equals(columnData.headName()) ? field.getName() : columnData.headName());
		}
	}


	/**
	 * 根据文件路径获取文件输出流
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	private static FileOutputStream getOutputStream(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			return  new FileOutputStream(file);
		} catch (Exception e) {
			logger.error("创建文件输出流时异常", e);
			throw new ReportException("创建文件输出流时异常", e);
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param outputStream
	 * @param es
	 */
	private static void close(SXSSFWorkbook workbook,OutputStream outputStream) {
		try {
			workbook.dispose();
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			logger.error("关闭输出流时异常~", e);
			throw new ReportException("关闭输出流时异常~", e);
		}
	}

}
