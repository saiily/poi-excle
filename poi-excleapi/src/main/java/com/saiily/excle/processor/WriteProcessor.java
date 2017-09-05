package com.saiily.excle.processor;

import java.util.List;

/**
 * 
 * 写处理类，主要用于设置页签名称和要写入的数据<br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-上午11:56:11 <br>
 * ===============================
 */
public class WriteProcessor<T> extends BaseProcessor<T> {

	// excle页签名称
	private String sheetName;

	public WriteProcessor() {
	}

	public WriteProcessor(List<T> list) {
		this.list = list;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
