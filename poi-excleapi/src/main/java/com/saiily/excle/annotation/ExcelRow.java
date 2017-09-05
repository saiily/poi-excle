package com.saiily.excle.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelRow {

	// 用于描述行之间行为，占多行还是一行，默认为1行
	int rowAcross() default 1;
}
