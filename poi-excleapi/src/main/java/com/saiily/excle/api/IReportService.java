/**
 * @(#)IReportService.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.api;

import java.io.InputStream;
import java.io.OutputStream;

import com.saiily.excle.processor.ReadProcessor;
import com.saiily.excle.processor.WriteProcessor;



/**
 * report服务接口，提供统一的接口，提供多种类型操作 <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-上午11:15:53 <br>
 * ===============================
 */
public interface IReportService {

	/**
	 * 指定文件路径读取
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetProcessors
	 *            读取处理
	 */
	void readReport(String filePath, ReadProcessor<?>... sheetProcessors);

	/**
	 * 根据输入流读取
	 * 
	 * @param inputStream
	 *            输入流
	 * @param sheetProcessors
	 *            读取处理
	 */
	void readReport(InputStream inputStream, ReadProcessor<?>... sheetProcessors);

	/**
	 * 根据字节流读取
	 * 
	 * @param bytes
	 *            字节流
	 * @param sheetProcessors
	 *            读取处理
	 */
	void readReport(byte[] bytes, ReadProcessor<?>... sheetProcessors);

	/**
	 * 指定文件路径生成文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetProcessors
	 *            写入处理
	 */
	void writeReport(String filePath, WriteProcessor<?>... sheetProcessors);

	/**
	 * 根据输出流生成文件
	 * 
	 * @param outputStream
	 *            输出流
	 * @param sheetProcessors
	 *            写入处理
	 */
	void writeReport(OutputStream outputStream, WriteProcessor<?>... sheetProcessors);

}
