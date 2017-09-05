/**
 * @(#)ReportException.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.exception;

/**
 *报表数据唯一性异常类
 *<br>===============================
 *<br>company：云徙科技[www.dtyunxi.com]
 *<br>author：su.xiaofeng@dtyunxi.com
 *<br>date：2017年8月14日-上午10:59:05
 *<br>===============================
 */
public class ReportUniqueException  extends RuntimeException{

	
	private static final long serialVersionUID = -6758152046832847719L;

	
	public ReportUniqueException(String msg) {
		super(msg);
	}
	
	public ReportUniqueException(String msg, Throwable e) {
		super(msg, e);
	}

	

}
