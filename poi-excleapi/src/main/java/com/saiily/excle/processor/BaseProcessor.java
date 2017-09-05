package com.saiily.excle.processor;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 * 处理基类，用于设置读写数据、页签索引、读写的起始行、分页设置等<br>
 * =============================== <br>
 * company：云徙科技[www.dtyunxi.com] <br>
 * author：su.xiaofeng@dtyunxi.com <br>
 * date：2017年8月14日-上午11:56:53 <br>
 * ===============================
 */
public class BaseProcessor<T> {

	// 默认分页数
	public static final int PAGE_SIZE = 1000;

	// excle页签（工作表）可以指定读取或者写入的页签位置 （注：读取多个页签时必须设置指定读取的页签），默认从第一个页签开始
	protected int sheetIndex = 0;

	// 从哪一行开始读写，默认从第二行开始写或读数据
	protected int rowStartIndex = 1;

	// excle页签（工作表）
	protected Sheet sheet;

	// 读取或写入的数据集合
	public List<T> list;

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public int getRowStartIndex() {
		return rowStartIndex;
	}

	public void setRowStartIndex(int rowStartIndex) {
		this.rowStartIndex = rowStartIndex;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
