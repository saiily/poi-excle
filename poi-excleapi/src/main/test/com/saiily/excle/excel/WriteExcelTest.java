/**
 * @(#)ReadExcelTest.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saiily.excle.api.IReportService;
import com.saiily.excle.api.ReportFactory;
import com.saiily.excle.constants.ReportConstants;
import com.saiily.excle.processor.WriteProcessor;
import com.saiily.excle.vo.ReportRegistryVo;
import com.saiily.excle.vo.TestVo;
import com.saiily.excle.vo.UserVo;



/**
 * 读取Excel测试类 <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-下午5:07:49 <br>
 * ===============================
 */
public class WriteExcelTest {

	public static void main(String[] args) throws Exception {

		IReportService reportService = ReportFactory.createReport(new ReportRegistryVo(ReportConstants.EXCEL));

		FileOutputStream outputStream = getOutputStream();
		
		WriteProcessor<TestVo> sheetTestProcessor = getTestProcessor();
		
		WriteProcessor<UserVo> sheetProcessor = getProcessor();
		
		sheetTestProcessor.setSheetName("好生活");
		//sheetProcessor.setSheetName("你好中国");
		//sheetTestProcessor.setSheetIndex(1);
		//sheetProcessor.setSheetIndex(0);
		//sheetTestProcessor.setRowStartIndex(2);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("开始时间："+simpleDateFormat.format(new Date()));
		//reportService.writeReport(outputStream,ExcelType.XLSX,sheetTestProcessor);
		
		reportService.writeReport("F:\\rr44.xlsx",sheetTestProcessor);
		

		System.out.println("生成文件成功！");
		System.out.println("结束时间："+simpleDateFormat.format(new Date()));
	}
	

	private static WriteProcessor<UserVo> getProcessor() {
		final List<UserVo> list = new ArrayList<UserVo>();
		for (int i = 0; i <400; i++) {
			UserVo user1 = new UserVo();
			user1.setId(i);
			user1.setName("tommy" + i);
			String buffer = "";
			if (i < 10) {
				buffer = "000";
			} else if (i < 100) {
				buffer = "00";
			} else if (i < 1000) {
				buffer = "0";
			}
			user1.setCardNo("6224123456" + buffer + i);
			user1.setSalary(new BigDecimal("10000").add(new BigDecimal(i)));
			user1.setPersonIns(new BigDecimal("400").add(new BigDecimal(i)));
			user1.setFirmIns(new BigDecimal("600").add(new BigDecimal(i)));
			user1.setPersonInsTitle("person");
			user1.setFirmInsTitle("firm");
			user1.setCreateTime(new Date());
			list.add(user1);
		}
		System.out.println(list.size());
		WriteProcessor<UserVo> sheetProcessor = new WriteProcessor<UserVo>(list){};
		return sheetProcessor;
	}
	
	private static WriteProcessor<TestVo> getTestProcessor() {
		final List<TestVo> list = new ArrayList<TestVo>();
		for (int i = 0; i <5000; i++) {
			TestVo user1 = new TestVo();
			user1.setId(i);
			user1.setName("tommy" + i);
			String buffer = "";
			if (i < 10) {
				buffer = "000";
			} else if (i < 100) {
				buffer = "00";
			} else if (i < 1000) {
				buffer = "0";
			}
			user1.setCardNo("6224123456" + buffer + i);
			user1.setSalary(new BigDecimal("10000").add(new BigDecimal(i)));
			user1.setCreateTime(new Date());
			list.add(user1);
		}
		System.out.println(list.size());
		WriteProcessor<TestVo> sheetProcessor = new WriteProcessor<TestVo>(list){};
		return sheetProcessor;
	}

	private static FileOutputStream getOutputStream() throws IOException, FileNotFoundException {
		String outputFilePath = "F:\\1111111.xls";
		File outputFile = new File(outputFilePath);
		outputFile.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		return outputStream;
	}
}
