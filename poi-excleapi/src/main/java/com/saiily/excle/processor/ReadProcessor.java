package com.saiily.excle.processor;

import java.util.List;

/**
 * 
 * 读处理类，主要用于返回读取的数据<br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-上午11:56:00 <br>
 * ===============================
 */
public class ReadProcessor<T> extends BaseProcessor<T> {

	public ReadProcessor() {
	}

	public ReadProcessor(List<T> list) {
		super.list = list;
	}

}
