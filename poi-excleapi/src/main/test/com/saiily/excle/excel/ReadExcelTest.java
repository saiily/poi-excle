/**
 * @(#)ReadExcelTest.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.saiily.excle.api.IReportService;
import com.saiily.excle.api.ReportFactory;
import com.saiily.excle.constants.ReportConstants;
import com.saiily.excle.exception.ReportUniqueException;
import com.saiily.excle.processor.BaseProcessor;
import com.saiily.excle.processor.ReadProcessor;
import com.saiily.excle.utils.ValidateUtils;
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
public class ReadExcelTest {

	public static void main(String[] args) throws Exception {
		
			IReportService reportService = ReportFactory.createReport(new ReportRegistryVo(ReportConstants.EXCEL));
			//FileInputStream inputStream = getFileInputStream();
			//ReadProcessor<UserVo> sheetProcessor = getProcessor();
			ReadProcessor<TestVo> sheetTestProcessor = getTestProcessor();
			//sheetProcessor.setSheetIndex(0);
			System.out.println(((ParameterizedType)new BaseProcessor<TestVo>(){}.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);   
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("开始时间："+simpleDateFormat.format(new Date()));
			String filePath = "F:\\rr44.xlsx";
			System.out.println(ValidateUtils.isExcel(filePath));
			
			try {
				reportService.readReport(filePath,sheetTestProcessor);
			} catch (ReportUniqueException e) {
				System.out.println("------------>>"+e.getMessage());
				//return;
			}
			/*for (UserVo info : sheetProcessor.getList()) {
				System.out.println("---sheetProcessor--->>"+info.toString());
			}
			for (TestVo info : sheetTestProcessor.getList()) {
				System.out.println(info.toString());
			}*/
			//System.out.println("--sheetProcessor---->>"+sheetProcessor.getList().size());
			System.out.println("---sheetTestProcessor--->>"+sheetTestProcessor.getList().size());
			
			System.out.println("读取文件成功！");
			System.out.println("结束时间："+simpleDateFormat.format(new Date()));
			
			String tt = "qwr";
			System.out.println(tt.indexOf("q"));
			
		
		
	}
	
	 // 将InputStream转换成byte数组  
    public static byte[] InputStreamTOByte(InputStream in) throws Exception {  
  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
  
        byte[] data = new byte[2048];  
  
        int count = -1;  
  
        while ((count = in.read(data, 0, 2048)) != -1){
        	 outStream.write(data, 0, count);  
        }
        return outStream.toByteArray();  
  
    }  
  

	/**
	 * @return
	 * @throws FileNotFoundException
	 */
	private static FileInputStream getFileInputStream() throws FileNotFoundException {
		final String filePath = "F:\\22222299.xls";
		File file = new File(filePath);
		FileInputStream inputStream = new FileInputStream(file);
		return inputStream;
	}

	private static ReadProcessor<UserVo> getProcessor() {
		ReadProcessor<UserVo> sheetProcessor = new ReadProcessor<UserVo>(){};
		return sheetProcessor;
	}

	
	private static ReadProcessor<TestVo> getTestProcessor() {
		ReadProcessor<TestVo> sheetProcessor = new ReadProcessor<TestVo>(){};
		return sheetProcessor;
	}
}
