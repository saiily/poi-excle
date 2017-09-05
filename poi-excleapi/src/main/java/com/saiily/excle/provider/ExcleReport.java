/**
 * @(#)ExcleReport.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.provider;

import java.io.InputStream;
import java.io.OutputStream;

import com.saiily.excle.api.AbstractReportService;
import com.saiily.excle.processor.ReadProcessor;
import com.saiily.excle.processor.WriteProcessor;
import com.saiily.excle.utils.ExcelReadUtils;
import com.saiily.excle.utils.ExcelWriteUtils;



/**
 *
 * <br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-下午2:47:59 <br>
 * ===============================
 */
public class ExcleReport extends AbstractReportService {

	@Override
	public void readReport(String filePath, ReadProcessor<?>... sheetProcessors) {
		ExcelReadUtils.readReport(filePath, sheetProcessors);
	}

	@Override
	public void readReport(InputStream inputStream, ReadProcessor<?>... sheetProcessors) {
		ExcelReadUtils.readReport(inputStream, sheetProcessors);
	}

	@Override
	public void readReport(byte[] bytes, ReadProcessor<?>... sheetProcessors) {
		ExcelReadUtils.readReport(bytes, sheetProcessors);
	}

	@Override
	public void writeReport(String filePath, WriteProcessor<?>... sheetProcessors) {
		ExcelWriteUtils.writeReport(filePath, sheetProcessors);
	}

	@Override
	public void writeReport(OutputStream outputStream, WriteProcessor<?>... sheetProcessors) {
		ExcelWriteUtils.writeReport(outputStream, sheetProcessors);
	}

}
