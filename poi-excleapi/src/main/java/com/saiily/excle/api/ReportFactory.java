/**
 * @(#)ReportFactory.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saiily.excle.constants.ReportConstants;
import com.saiily.excle.provider.ExcleReport;
import com.saiily.excle.vo.ReportRegistryVo;



/**
 * report工厂主要是用于创建reportService服务 <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-下午2:23:57 <br>
 * ===============================
 */

public class ReportFactory {

	private static Logger logger = LoggerFactory.getLogger(ReportFactory.class);

	public static IReportService createReport(ReportRegistryVo reportRegistryVo) {
		AbstractReportService reportService = null;

		if (reportRegistryVo != null && reportRegistryVo.getReportType() != null) {
			switch (reportRegistryVo.getReportType().toUpperCase()) {
			case ReportConstants.EXCEL:
				reportService = new ExcleReport();
				break;
			case ReportConstants.DOCS:
				// reportService = new DocsReport();
				break;
			case ReportConstants.PDF:
				// reportService = new PdfReport();
				break;
			default:
			}
		}

		if (reportService == null) {
			logger.error("未指定ReportService类型");
			throw new IllegalArgumentException("未指定ReportService类型");
		}

		reportService.init(reportRegistryVo);
		return reportService;
	}
}
