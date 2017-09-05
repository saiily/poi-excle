package com.saiily.excle.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.saiily.excle.annotation.ExcelColumn;
import com.saiily.excle.annotation.ExcelRow;
import com.saiily.excle.enums.DealType;
import com.saiily.excle.exception.ReportException;
import com.saiily.excle.exception.ReportUniqueException;
import com.saiily.excle.processor.BaseProcessor;
import com.saiily.excle.processor.ReadProcessor;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ExcelReadUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExcelReadUtils.class);

	/**
	 * 根据文件路径读取文件数据
	 * 
	 * @param filePath
	 * @param sheetProcessors
	 */
	public static void readReport(String filePath, ReadProcessor<?>... sheetProcessors) {
		// 检查文件路径是否合法
		if (ValidateUtils.validateExcel(filePath)) {
			readReport(getFileInputStream(filePath), sheetProcessors);
		} else {
			throw new ReportException("读取的文件不是EXCLE格式或者文件不存在");
		}
	}

	/**
	 * 根据文件二进流读取文件数据
	 * 
	 * @param bytes
	 * @param sheetProcessors
	 */
	public static void readReport(byte[] bytes, ReadProcessor<?>... sheetProcessors) {
		readReport(byteTOInputStream(bytes), sheetProcessors);
	}

	/**
	 * 根据文件输入流读取文件数据
	 * 
	 * @param inputStream
	 * @param sheetProcessors
	 */
	public static void readReport(InputStream inputStream, ReadProcessor<?>... sheetProcessors) {
		// 使用线程池进行线程管理。
		ExecutorService es = Executors.newCachedThreadPool();
		// 使用计数栅栏
		CountDownLatch doneSignal = null;
		// 对数据唯一性处理
		Map<String, String> uniqueMap = new HashMap<>();
		try {
			// 创建工作簿workBook
			Workbook workbook = WorkbookFactory.create(inputStream);

			for (ReadProcessor<?> sheetProcessor : sheetProcessors) {
				// 读取工作簿哪个页签，默认是从第一个页签开始
				Integer sheetIndex = sheetProcessor.getSheetIndex();

				Sheet sheet = workbook.getSheetAt(sheetIndex);
				sheetProcessor.setSheet(sheet);
				// 分页数
				int pageSize = BaseProcessor.PAGE_SIZE;
				// 从哪行开始读取数据
				int readRowIndex = sheetProcessor.getRowStartIndex();

				Class<?> clazz = (Class<?>) ((ParameterizedTypeImpl) sheetProcessor.getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];

				// 要读取的总行数据
				int totalRowNum = sheet.getLastRowNum() - readRowIndex + 1;
				ExcelRow rowData = clazz.getAnnotation(ExcelRow.class);
				// 判断页面是否跨行情况
				if (rowData != null && rowData.rowAcross() > 1) {
					totalRowNum = new Double(Math.ceil(totalRowNum / rowData.rowAcross())).intValue();
				}
				// 分批数
				int part = (totalRowNum % pageSize == 0) ? (totalRowNum / pageSize) : (totalRowNum / pageSize + 1);
				logger.info("共有 ：{}条数据，分为 ：{}批", totalRowNum, part);
				doneSignal = new CountDownLatch(part);
				List listPage = new ArrayList();

				for (int i = 0; i < part; i++) {
					int startRowIndex = getStartRowIndex(pageSize, readRowIndex, rowData, i);
					int readRowNum = getReadRowNum(pageSize, totalRowNum, part, i);
					es.submit(new PoiReader(doneSignal, sheetProcessor, listPage, clazz, readRowNum, startRowIndex,
							uniqueMap));
				}
				// 使用CountDownLatch的await方法，等待所有线程完成sheet操作
				doneSignal.await();
			}
			if (!CollectionUtils.isEmpty(uniqueMap)) {
				throw new ReportUniqueException(getMsg(uniqueMap));
			}
			logger.info("读取Excle文件成功~");
		} catch (ReportUniqueException e) {
			throw new ReportUniqueException(e.getMessage());
		} catch (Exception e) {
			logger.error("读取表格数据时异常,msg:{}", e.getMessage());
			throw new ReportException("读取表格数据时异常", e);
		} finally {
			close(inputStream, es);
		}

	}

	/**
	 * 进行sheet写操作的sheet。
	 * 
	 */
	protected static class PoiReader implements Runnable {

		private final CountDownLatch doneSignal;

		private ReadProcessor<?> sheetProcessor;
		private List listPage;
		private Class<?> clazz;
		private int readRowNum;
		private int readRowIndex;
		private Map<String, String> uniqueMap;

		public PoiReader(CountDownLatch doneSignal, ReadProcessor<?> sheetProcessor, List listPage, Class<?> clazz,
				int readRowNum, int readRowIndex, Map<String, String> uniqueMap) {
			this.doneSignal = doneSignal;
			this.sheetProcessor = sheetProcessor;
			this.listPage = listPage;
			this.clazz = clazz;
			this.readRowNum = readRowNum;
			this.readRowIndex = readRowIndex;
			this.uniqueMap = uniqueMap;
		}

		public void run() {
			try {
				read(sheetProcessor, listPage, clazz, readRowNum, readRowIndex, uniqueMap);
				sheetProcessor.setList(listPage);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				doneSignal.countDown();
			}
		}
	}

	private static int read(ReadProcessor<?> sheetProcessor, List listPage, Class<?> clazz, int size, int readRowIndex,
			Map<String, String> uniqueMap) throws Exception {
		ExcelRow rowData = clazz.getAnnotation(ExcelRow.class);
		Sheet sheet = sheetProcessor.getSheet();
		// 创建临时map,用于保存是否重复数据
		Map<String, String> tempMap = new HashMap<>();
		for (int i = 0; i < size; i++) {
			List<Row> rowList = new ArrayList<Row>();
			for (int j = 0; rowData != null && j < rowData.rowAcross() || j < 1; j++) {
				Row row = sheet.getRow(readRowIndex + j);
				if (row == null) {
					continue;
				}
				rowList.add(row);
			}
			Field[] declaredFields = clazz.getDeclaredFields();
			Object obj = clazz.newInstance();
			for (Field field : declaredFields) {
				ExcelColumn column = null;
				PropertyDescriptor pd = null;
				try {
					column = field.getAnnotation(ExcelColumn.class);
					if (null == column) {
						continue;
					}
					synchronized (logger) {

						Cell cell = rowList.get(column.rowIndex()).getCell(column.colIndex());
						Object fieldVal = getCell(cell, field.getType());
						if (column.isUnique()) {// 是唯一值
							// 在excel中，计数是从0开始的，为了使结果与Excel中显示的行数保持一致，让行数+1
							checkUnique(readRowIndex + 1, uniqueMap, tempMap, field.getName(), fieldVal);
						}
						pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
						pd.getWriteMethod().invoke(obj, fieldVal);
					}
				} catch (Exception e) {

					if (column.dealType().equals(DealType.CONTINUE_CELL)) {
						continue;
					}
					if (column.dealType().equals(DealType.CONTINUE_ROW)) {
						break;
					}
					logger.error("实体字段时异常,fieldName:{}", field.getName() + i);
					throw new ReportException("deal cell exception", e);
				}
			}
			listPage.add(obj);
			if (rowData != null) {
				readRowIndex = readRowIndex + rowData.rowAcross();
			} else {
				readRowIndex++;
			}
		}

		return readRowIndex;
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param bytes
	 * @return
	 */
	public static InputStream byteTOInputStream(byte[] bytes) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		return byteArrayInputStream;
	}

	/**
	 * 获取唯一值信息
	 * 
	 * @param uniqueMap
	 * @return
	 */
	private static String getMsg(Map<String, String> uniqueMap) {
		StringBuilder msg = new StringBuilder();
		for (Map.Entry<String, String> map : uniqueMap.entrySet()) {
			msg.append(map.getKey()).append(map.getValue()).append("行\n");
		}
		return msg.toString();
	}

	/**
	 * 检查数据的唯一性
	 * 
	 * @param readRowIndex
	 * @param uniqueMap
	 * @param tempMap
	 * @param fieldName
	 * @param fieldVal
	 */
	private static void checkUnique(int readRowIndex, Map<String, String> uniqueMap, Map<String, String> tempMap,
			String fieldName, Object fieldVal) {
		String key = getKey(fieldName, fieldVal);
		if (tempMap.containsKey(key)) {
			// 拿到先前保存的行号
			String rowNum = tempMap.get(key);
			if (uniqueMap.containsKey(key)) {
				// 拿到先前保存的所有行号记录
				String str = uniqueMap.get(key);
				// 更新后，显示效果：——》行重复：在第 2 ，3 , 5
				uniqueMap.put(key, str + "," + readRowIndex);
			} else {
				// 最后显示效果：——》行重复：在第 2 ，3
				uniqueMap.put(key, getValue(rowNum, readRowIndex));
			}
		}
		tempMap.put(key, String.valueOf(readRowIndex));
	}

	private static String getKey(String fieldName, Object fieldVal) {
		return new StringBuffer("字段为：").append(fieldName).append(",值为：").append(fieldVal).toString();
	}

	private static String getValue(String rowNum, int readRowIndex) {
		return new StringBuffer(",存在重复数据：行数位于第 ").append(rowNum).append(",").append(readRowIndex).toString();
	}

	/**
	 * 设置单元格数据
	 * 
	 * @param cell
	 * @param clazz
	 * @return
	 */
	private static Object getCell(Cell cell, Class<?> clazz) {
		String cellValue;
		try {
			cellValue = cell.getStringCellValue();
		} catch (Exception e) {
			double numericCellValue = cell.getNumericCellValue();
			cellValue = String.valueOf(numericCellValue);
			if (cellValue.indexOf("E") != -1) { // 解决科学计数法
				DecimalFormat df = new DecimalFormat("0");
				cellValue = df.format(numericCellValue);
			}
			if (cellValue.endsWith(".0")) { // 解决数字带.0
				DecimalFormat df = new DecimalFormat("0");
				cellValue = df.format(numericCellValue);
			}
		}
		if (clazz.equals(short.class) || clazz.equals(Short.class)) {
			return Double.valueOf(cellValue).shortValue();
		} else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
			return Double.valueOf(cellValue).intValue();
		} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
			return Double.valueOf(cellValue).longValue();
		} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
			return Float.valueOf(cellValue);
		} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
			return Double.valueOf(cellValue);
		} else if (clazz.equals(Date.class)) {
			return cell.getDateCellValue();
		} else if (clazz.equals(BigDecimal.class)) {
			return new BigDecimal(cellValue);
		} else {
			return cellValue;
		}
	}

	/**
	 * 获取读取的row个数
	 * 
	 * @param pageSize
	 * @param totalRowNum
	 * @param part
	 * @param i
	 * @return
	 */
	private static int getReadRowNum(int pageSize, int totalRowNum, int part, int i) {
		int size;
		if (1 == part) {
			size = totalRowNum;
		} else if (i == part - 1) {
			size = totalRowNum - (pageSize * (part - 1));
		} else {
			size = pageSize;
		}
		return size;
	}

	/**
	 * 获取读取文件的起始行
	 * 
	 * @param pageSize
	 * @param readRowIndex
	 * @param rowData
	 * @param i
	 * @return
	 */
	private static int getStartRowIndex(int pageSize, int readRowIndex, ExcelRow rowData, int i) {
		int startRowIndex = i * pageSize + readRowIndex;
		if (rowData != null && rowData.rowAcross() > 1) {
			startRowIndex = (i * pageSize * rowData.rowAcross()) + readRowIndex;
		}
		return startRowIndex;
	}

	/**
	 * 关闭连接
	 * 
	 * @param inputStream
	 * @param es
	 */
	private static void close(InputStream inputStream, ExecutorService es) {
		es.shutdown();
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error("关闭输入流时异常~", e);
				throw new ReportException("关闭输入流时异常~", e);
			}
		}
	}

	private static FileInputStream getFileInputStream(String filePath) {
		try {
			return new FileInputStream(filePath);
		} catch (Exception e) {
			logger.error("创建文件输入流时异常", e);
			throw new ReportException("创建文件输入流时异常", e);
		}
	}

}
