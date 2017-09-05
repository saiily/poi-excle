/**
 * @(#)ReportRegistryVo.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.vo;

/**
 * <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-下午2:31:31 <br>
 * ===============================
 */
public class ReportRegistryVo {

	// 报表类型
	private String reportType;

	public ReportRegistryVo() {
	}

	public ReportRegistryVo(String reportType) {
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

}
