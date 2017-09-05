/**
 * @(#)AbstractReportService.java 1.0 2017年8月14日
 *
 * Copyright (c) 2016, YUNXI. All rights reserved.
 * YUNXI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.saiily.excle.api;

import com.saiily.excle.vo.ReportRegistryVo;

/**
 * report抽象类：主要功能是抽取公共的方法和公共属性<br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-下午2:21:34 <br>
 * ===============================
 */
public abstract class AbstractReportService implements IReportService {

	protected ReportRegistryVo reportRegistryVo;

	public void init(ReportRegistryVo reportRegistryVo) {
		this.reportRegistryVo = reportRegistryVo;
	}

}
