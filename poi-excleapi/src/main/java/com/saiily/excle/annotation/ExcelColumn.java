package com.saiily.excle.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.saiily.excle.enums.DealType;



@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

	// 表格列索引，默认从第一列开始
	int colIndex();
	
	// 表格行索引，列数据存在跨行时使用
	int rowIndex() default 0;

	//对数据进行转换处理，（1）数据类型  format="#,##0.00" (2)时间类型   format="yyyy/mm/dd hh:mm"
	String format() default "";

	//是否跨行处理，默认不进行跨行操作
	boolean isAcross() default false;
	
	//是否唯一值处理，默认不处理
	boolean isUnique() default false;
	
	//一列占多少行，默认为1行
	int rowAcross() default 1;

	// execl时的表头名称，不设置时把属性名作表头
	String headName() default "";
	
	//如果字段和单元格不匹配，设置为NULL并继续处理其他单元格
	DealType dealType() default DealType.CONTINUE_CELL;

}
