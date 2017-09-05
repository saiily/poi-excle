/**
 * @(#)DealType.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.enums;


/**
 * 对excle的处理类型
 *<br>===============================
 *<br>company：云徙科技[www.dtyunxi.com]
 *<br>author：su.xiaofeng@dtyunxi.com
 *<br>date：2017年8月14日-上午10:54:34
 *<br>===============================
 */
public enum DealType {

	/**
	 * 
	 * 如果字段和单元格不匹配，设置为NULL并继续处理其他单元格
	 */
	CONTINUE_CELL,

	/**
	 * 如果字段和单元格不匹配，设置为NULL并继续处理其他行
	 */
	CONTINUE_ROW,

	/**
	 * 如果字段和单元格不匹配，则中止进程
	 */
	ABORT,
}
