package com.saiily.excle.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.saiily.excle.annotation.ExcelColumn;
import com.saiily.excle.annotation.ExcelRow;


@ExcelRow(rowAcross = 1)
public class TestVo {

	@ExcelColumn(colIndex = 0, format = "0")
	private int id;
	@ExcelColumn(colIndex = 1, headName = "名字",isUnique=true)
	private String name;

	@ExcelColumn(colIndex = 2, headName = "身份证号",isUnique=true)
	private String cardNo;

	@ExcelColumn(colIndex = 3, format = "#,##0.00")
	private BigDecimal salary;

	@ExcelColumn(colIndex = 4, headName = "创建时间", format = "yyyy/mm/dd")
	private Date createTime;

	private String myAge;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public String getMyAge() {
		return myAge;
	}

	public void setMyAge(String myAge) {
		this.myAge = myAge;
	}

	@Override
	public String toString() {
		return "TestVo [id=" + id + ", name=" + name + ", cardNo=" + cardNo + ", salary=" + salary + ", createTime="
				+ createTime + "]";
	}

}
