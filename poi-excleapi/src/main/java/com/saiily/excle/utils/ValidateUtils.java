/**
 * @(#)ValidateUtils.java 1.0 2017年8月24日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月24日-上午9:40:06 <br>
 * ===============================
 */
public class ValidateUtils {

	private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

	/**
	 * 根据文件路径判断是否是Excle文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static boolean isExcel(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls|xlsx)$");
	}

	/**
	 * 检查文件是否合法
	 * 
	 * @param filePath
	 * @param errorInfo
	 * @return
	 */
	public static boolean validateExcel(String filePath) {
		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		if (filePath == null || !(isExcel(filePath))) {
			logger.info("读取文件名不是excel格式:{}", filePath);
			return false;
		}
		/** 检查文件是否存在 */
		File file = new File(filePath);
		if (!file.exists()) {
			logger.info("读取文件不存在:{}", filePath);
			return false;
		}
		return true;
	}
}
